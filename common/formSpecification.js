const fieldTypes = {
  'ATTACHMENT': 'upload',
  'CHECKBOX': 'multipleChoice',
  'MATRIX_CHECKBOX': 'multipleChoice',
  'RADIO': 'radio',
  'MATRIX_RADIO': 'radio',
  'SELECT': 'radio',
  'QUESTION': 'text',
  'QUESTION_MULTILINE': 'text',
}

function formSpec(answerJson) {
  const { form: { pages } } = answerJson
  const allElements = pages.reduce((a, b) => [...a, ...b.elements], [])
  const allQuestions = allElements.reduce((a, b) => [...a, ...(
    b.questions.map(question => ({...question, type: fieldTypes[b.elementType] || 'unknown' }))
  )], [])
  const questionFields = allQuestions.map(question => {
    const options = question.answerOptions.length > 0 ? {
      options: question.answerOptions.map(option => ({
        id: option.answerOptionId,
        name: option.externalAnswerOptionId,
      })),
    } : {}
    return {
      name: question.externalQuestionId,
      type: question.type,
      questionId: question.questionId,
      required: question.mandatory,
      ...options,
    }})
  return {
    form: { id: answerJson.form.formId },
    fields: questionFields,
  }
}

export default formSpec
