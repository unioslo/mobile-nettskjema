class UploadQuestion: FormField {
    private let id: Int

    init(id: Int) {
        self.id = id
    }
    
    var identifier: FieldIdentifier {
        get {
            return NettskjemaFieldIdentifier(nettskjemaName: "attachment.upload", id: id)
        }
    }
}
