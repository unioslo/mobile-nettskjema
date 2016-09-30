class NettskjemaForm: Form {
    private let id: Int

    init(id: Int) {
        self.id = id
    }
    
    var postUrl: String {
        get {
            return "https://nettskjema.uio.no/answer/deliver.json?formId=" + String(id)
        }
    }

    
}
