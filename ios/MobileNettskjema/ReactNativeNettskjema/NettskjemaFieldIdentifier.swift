class NettskjemaFieldIdentifier: FieldIdentifier {
    private let nettskjemaName: String
    private let id: Int
    
    init(nettskjemaName: String, id: Int) {
        self.nettskjemaName = nettskjemaName
        self.id = id
    }
    
    var name: String {
        get {
            return "answersAsMap[" + String(id) + "]." + nettskjemaName
        }
    }
}
