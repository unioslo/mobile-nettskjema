class TextQuestion: FormField {
    private let id: Int
    
    init(id: Int) {
        self.id = id
    }
    
    var identifier: FieldIdentifier {
        get {
            return NettskjemaFieldIdentifier(nettskjemaName: "textAnswer", id: id)
        }
    }
    
}
