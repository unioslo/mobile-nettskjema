class NettskjemaRNFields {
    private var deserializedFields: [FilledInFormField] = []
    
    init(fields: [[String: AnyObject]]) throws {
        for field in fields {
            try deserializedFields.append(NettskjemaRNField(field: field).deserialized())
        }
    }
    
    var asList: [FilledInFormField] {
        get {
            return deserializedFields
        }
    }
}
