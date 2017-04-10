import should from  'should'
import formSpec from '../common/formSpecification'
import submissionCreator from '../common/submissionCreator'

describe('Form specification parser', () => {
  it('works as expected', () => {
    const expected = {
      form: { id: 75319 },
      fields: [
        {
          type: "text",
          questionId: 577795,
          name: 'posted-by',
          required: true,
        },
        {
          type: "radio",
          questionId: 578095,
          name: 'radio-questions',
          required: false,
          options: [
            { id: 1275519, name: 'select-this-radio' },
            { id: 1275520, name: 'unselect-this-radio' },
          ],
        },
        {
          type: "multipleChoice",
          questionId: 578096,
          name: 'multiplechoice-questions',
          required: false,
          options: [
            { id: 1275521, name: 'select-this-multi-1' },
            { id: 1275522, name: 'select-this-multi-2' },
            { id: 1275523, name: 'unselect-this-multi' },
          ],
        },
        {
          name: "att-1",
          questionId: 577797,
          required: false,
          type: "upload",
        },
        {
          name: "att-2",
          questionId: 577798,
          required: false,
          type: "upload",
        },
      ],
    }
    formSpec(sampleAnswerJson).should.deepEqual(expected)
  })
})

describe('Form submission creator', () => {
  it('creates a valid submission from valid input data', () => {
    const expected = {
      form: { id: 75319 },
      fields: [
        {
          type: "text",
          questionId: 577795,
          answer: 'ExampleText',
        },
        {
          type: "radio",
          questionId: 578095,
          selectedOptionId: 1275519,
        },
        {
          type: "multipleChoice",
          questionId: 578096,
          selectedOptionId: 1275521,
        },
        {
          type: "multipleChoice",
          questionId: 578096,
          selectedOptionId: 1275522,
        },
        {
          filepath: "attachmentPath1",
          questionId: 577797,
          type: "upload",
          mediaType: 'html',
        },
        {
          filepath: "attachmentPath2",
          questionId: 577798,
          type: "upload",
          mediaType: 'pdf',
        },
      ],
    }
    const spec = formSpec(sampleAnswerJson)
    const actual = submissionCreator(spec)({
      "posted-by": "ExampleText",
      "radio-questions": "select-this-radio",
      "multiplechoice-questions": ['select-this-multi-1', 'select-this-multi-2'],
      "att-1": { filePath: "attachmentPath1", mimeType: 'html' },
      "att-2": { filePath: "attachmentPath2", mimeType: 'pdf' },
    })
    actual.should.deepEqual(expected)
  })
  it('throws if a required field is missing', () => {
    const spec = formSpec(sampleAnswerJson)
    should.throws(() => {
      submissionCreator(spec)({
        "radio-questions": "select-this-radio",
        "multiplechoice-questions": ['select-this-multi-1', 'select-this-multi-2'],
        "att-1": "attachmentPath1",
        "att-2": "attachmentPath2",
      })
    })
  })
  it('throws if a provided field is not in spec', () => {
    const spec = formSpec(sampleAnswerJson)
    should.throws(() => {
      submissionCreator(spec)({
        "posted-by": "sampleApp",
        "nonsense-field": "boo!",
      })
    })
  })
  it('throws if given non-string input for a text field', () => {
    const spec = formSpec(sampleAnswerJson)
    should.throws(() => { submissionCreator(spec)({
      "posted-by": 123,
    })
    })
  })
  it('throws if given non-string input for an attachment field', () => {
    const spec = formSpec(sampleAnswerJson)
    should.throws(() => { submissionCreator(spec)({
      "posted-by": "valid",
      "att-1": 123,
    })
    })
  })
  it('throws if given invalid name for a radio option', () => {
    const spec = formSpec(sampleAnswerJson)
    should.throws(() => {
      submissionCreator(spec)({
        "posted-by": "valid",
        "radio-questions": "option-that-does-not-exist",
      })
    })
  })
  it('throws if given invalid name for a multiplechoice option', () => {
    const spec = formSpec(sampleAnswerJson)
    should.throws(() => {
      submissionCreator(spec)({
        "posted-by": "test",
        "multiplechoice-questions": ["option-that-does-not-exist"],
      })
    })
  })
  it('throws if given a non-array for the multiplechoice options', () => {
    const spec = formSpec(sampleAnswerJson)
    should.throws(() => {
      submissionCreator(spec)({
        "posted-by": "test",
        "multiplechoice-questions": "just-a-string",
      })
    })
  })
  it('throws if given an array of non-strings the multiplechoice options', () => {
    const spec = formSpec(sampleAnswerJson)
    should.throws(() => {
      submissionCreator(spec)({
        "posted-by": "test",
        "multiplechoice-questions": [123, 456],
      })
    })
  })
  it('throws if spec contains an unknown field type', () => {
    const spec = {
      form: { id: 1234 },
      fields: [
        {
          type: "unknown",
          name: "FieldOfMysticType",
          questionId: 4321,
        },
      ],
    }
    should.throws(() => {
      submissionCreator(spec)()
    })
  })
})

const sampleAnswerJson = {
  "form": {
    "expiredToken": null,
    "authenticationRequired": false,
    "delivered": false,
    "formId": 75319,
    "answers": {
      "577795": {
        "answerOptionIds": []
      },
      "577797": {
        "answerOptionIds": []
      },
      "577798": {
        "answerOptionIds": []
      },
      "578095": {
        "answerOptionIds": []
      },
      "578096": {
        "answerOptionIds": []
      }
    },
    "submissionId": null,
    "pages": [
      {
        "pageId": 44174,
        "elements": [
          {
            "elementId": 613855,
            "elementType": "QUESTION",
            "dateFormat": null,
            "nationalIdNumberType": null,
            "maxSelectedAnswerOptions": null,
            "description": null,
            "title": null,
            "questions": [
              {
                "questionId": 577795,
                "text": "Posta av",
                "description": null,
                "mandatory": true,
                "horizontal": false,
                "sendAdditionalRecipientEmail": false,
                "externalQuestionId": "posted-by",
                "answerOptions": []
              }
            ],
            "answerOptions": [],
            "visibilityAnswerOptionIds": [],
            "validationScript": null
          },
          {
            "elementId": 614138,
            "elementType": "RADIO",
            "dateFormat": null,
            "nationalIdNumberType": null,
            "maxSelectedAnswerOptions": null,
            "description": null,
            "title": null,
            "questions": [
              {
                "questionId": 578095,
                "text": "Radiospørsmål",
                "description": null,
                "mandatory": false,
                "horizontal": false,
                "sendAdditionalRecipientEmail": false,
                "externalQuestionId": "radio-questions",
                "answerOptions": [
                  {
                    "answerOptionId": 1275519,
                    "text": "Skal veljast",
                    "preselected": false,
                    "externalAnswerOptionId": "select-this-radio",
                    "imageUrl": null,
                    "altText": null,
                    "image": null
                  },
                  {
                    "answerOptionId": 1275520,
                    "text": "Lat i fred!",
                    "preselected": false,
                    "externalAnswerOptionId": "unselect-this-radio",
                    "imageUrl": null,
                    "altText": null,
                    "image": null
                  }
                ]
              }
            ],
            "answerOptions": [],
            "visibilityAnswerOptionIds": [],
            "validationScript": null
          },
          {
            "elementId": 614139,
            "elementType": "CHECKBOX",
            "dateFormat": null,
            "nationalIdNumberType": null,
            "maxSelectedAnswerOptions": 0,
            "description": null,
            "title": null,
            "questions": [
              {
                "questionId": 578096,
                "text": "Fleirvalsspørsmål",
                "description": null,
                "mandatory": false,
                "horizontal": false,
                "sendAdditionalRecipientEmail": false,
                "externalQuestionId": "multiplechoice-questions",
                "answerOptions": [
                  {
                    "answerOptionId": 1275521,
                    "text": "Vel dette",
                    "preselected": false,
                    "externalAnswerOptionId": "select-this-multi-1",
                    "imageUrl": null,
                    "altText": null,
                    "image": null
                  },
                  {
                    "answerOptionId": 1275522,
                    "text": "Og vel dette",
                    "preselected": false,
                    "externalAnswerOptionId": "select-this-multi-2",
                    "imageUrl": null,
                    "altText": null,
                    "image": null
                  },
                  {
                    "answerOptionId": 1275523,
                    "text": "Men ikkje dette",
                    "preselected": false,
                    "externalAnswerOptionId": "unselect-this-multi",
                    "imageUrl": null,
                    "altText": null,
                    "image": null
                  }
                ]
              }
            ],
            "answerOptions": [],
            "visibilityAnswerOptionIds": [],
            "validationScript": null
          },
          {
            "elementId": 613857,
            "elementType": "ATTACHMENT",
            "dateFormat": null,
            "nationalIdNumberType": null,
            "maxSelectedAnswerOptions": null,
            "description": null,
            "title": null,
            "questions": [
              {
                "questionId": 577797,
                "text": "Vedlegg 1",
                "description": null,
                "mandatory": false,
                "horizontal": false,
                "sendAdditionalRecipientEmail": false,
                "externalQuestionId": "att-1",
                "answerOptions": []
              }
            ],
            "answerOptions": [],
            "visibilityAnswerOptionIds": [],
            "validationScript": null
          },
          {
            "elementId": 613858,
            "elementType": "ATTACHMENT",
            "dateFormat": null,
            "nationalIdNumberType": null,
            "maxSelectedAnswerOptions": null,
            "description": null,
            "title": null,
            "questions": [
              {
                "questionId": 577798,
                "text": "Vedlegg 2",
                "description": null,
                "mandatory": false,
                "horizontal": false,
                "sendAdditionalRecipientEmail": false,
                "externalQuestionId": "att-2",
                "answerOptions": []
              }
            ],
            "answerOptions": [],
            "visibilityAnswerOptionIds": [],
            "validationScript": null
          }
        ]
      }
    ],
    "invitationId": null,
    "postponable": false,
    "clientPostponable": false,
    "type": "DEFAULT"
  },
  "status": "success"
}
