import Foundation

protocol Pipe {
    func connect(inputFile: NSURL, outputFile: NSURL) throws
}

class InplaceFileTransformation {
    private let originalFile: NSURL
    private let transformingPipe: Pipe
    
    init(originalFile: NSURL, transformingPipe: Pipe) {
        self.originalFile = originalFile
        self.transformingPipe = transformingPipe
    }
    
    func perform() throws {
        let tempFile = TemporaryFile(file: originalFile)
        try transformingPipe.connect(originalFile, outputFile: tempFile.tempFile)
        try tempFile.replaceOriginalFile()
    }
}
