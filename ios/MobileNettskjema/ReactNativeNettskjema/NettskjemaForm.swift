class NettskjemaForm: Form {
    private let id: Int

    init(id: Int) {
        self.id = id
    }
    
    init(serialized: [String : AnyObject]) {
        self.id = serialized["form"] as! Int
    }
    
    var postUrl: String {
        get {
            return "https://nettskjema.uio.no/answer/deliver.json?formId=" + String(id)
        }
    }
    
    var serialized: [String : AnyObject] {
        get {
            return ["form": id]
        }
    }

    
}
