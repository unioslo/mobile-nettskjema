function submissionCreator(spec) {

  return (fieldNameValueMap={}) => {
    for (const providedFieldName of Object.keys(fieldNameValueMap)) {
      if (spec.fields.find(field => field.name === providedFieldName) == null) {
        throw `Provided field '${providedFieldName}' does not exist in form specification`
      }
    }

    let fields = []
    for (const specField of spec.fields) {
      let addFields = []
      const { type, questionId, name, required } = specField
      const value = fieldNameValueMap[name]
      if ((value == null || value === "") && required) {
        throw `Value not provided for field of type '${type}' named '${name}'`
      }
      const basicFieldValues = {
        type: type,
        questionId: questionId,
      }
      if (type === "text"){
        const valueType = typeof value
        if (value != null && valueType !== "string") {
          throw `Value for text field must be of a string, instead got an instance of '${valueType}'`
        }
        if (value != null) {
          addFields = [{
            ...basicFieldValues,
            answer: value.toString(),
          }]
        }
      }
      else if (type === "upload"){
        if (value != null) {
          const { filePath, mimeType } = value
          if (filePath == null || typeof filePath !== "string") {
            throw `'filepath' (string) must be provided for attachment`
          }
          if (mimeType == null || typeof mimeType !== "string") {
            throw `'mimeType' (string) must be provided for attachment`
          }
          addFields = [{
            ...basicFieldValues,
            filepath: filePath,
            mediaType: mimeType,
          }]
        }
      }
      else if (type === "radio") {
        const optionName = value
        if (optionName != null) {
          const selectedOption = specField.options.find(opt => opt.name === optionName)
          if (selectedOption == null) {
            throw `Selected option must be one of [${specField.options.map(opt=>`'${opt.name}'`).toString()}], instead got '${optionName}'`
          }
          addFields = [{
            ...basicFieldValues,
            selectedOptionId: selectedOption.id,
          }]
        }
      }
      else if (type === "multipleChoice") {
        const optionNames = value
        if (optionNames != null) {
          if (!Array.isArray(optionNames)) {
            throw `Selected options for multiple choice field '${name}' must be provided in an array, instead got an instance of ${typeof optionNames}`
          }
          for (const selectedOption of optionNames) {
            if (specField.options.find(opt=>opt.name === selectedOption) == null) {
              throw `Selected option must be one of [${specField.options.map(opt=>`'${opt.name}'`).toString()}], instead got '${selectedOption}'`
            }
          }
          const selectedOptions = specField.options.filter(opt => optionNames.indexOf(opt.name) !== -1)
          addFields = selectedOptions.map(opt => ({
            ...basicFieldValues,
            selectedOptionId: opt.id,
          }))
        }
      }
      else {
        throw `Unknown type of field '${name}' in spec: '${type}'`
      }

      fields = [...fields, ...addFields]
    }

    return {
      form: spec.form,
      fields,
    }
  }
}

export default submissionCreator
