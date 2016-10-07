import Foundation

class NettskjemaJsonFields {
    private var deserializedFields: [FilledInFormField] = []

    init(fields: NSArray) throws {
        for field in fields {
            try deserializedFields.append(NettskjemaJsonField(field: field as! [String: AnyObject]).deserialized())
        }
    }
    
    var asList: [FilledInFormField] {
        get {
            return self.deserializedFields
        }
    }
}
