#import "RNNettskjema.h"
#import "JSEventSink.h"
@import MobileNettskjemaIOS;


@implementation RNNettskjema

{
    MobileNettskjema *mobileNettskjema;
}

- (id)init {
    self = [super init];
    if (self) {
        LibraryCacheStorageDirectory *directory = [[LibraryCacheStorageDirectory alloc] init];
        JSEventSink *eventSink = [[JSEventSink alloc] initWithEmitter:&*self];
        mobileNettskjema = [[MobileNettskjema alloc] initWithStorageDirectory:directory eventSink:eventSink];
    }
    return self;
}

- (NSArray<NSString *>*)supportedEvents {
    return @[ @"submissionStateChanged"];
}


RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(addToSubmissionQueue:(NSDictionary *)submission resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSError *error;
    BOOL success = [mobileNettskjema addToSubmissionQueue:submission error:&error onFirstProcessingComplete:^{ }];
    if (!success) {
        reject(@"add_to_queue_failed", @"Adding submission to queue failed", error);
    } else {
        resolve(nil);
    }
}

RCT_EXPORT_METHOD(clearTemporaryFiles:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSError *error;
    BOOL success = [mobileNettskjema clearTemporaryFilesAndReturnError:&error];
    if (!success) {
        reject(@"clear_temporary_files_failed", @"Clear temporary files failed", error);
    } else {
        resolve(nil);
    }
}

RCT_EXPORT_METHOD(forceRetryAllSubmissions:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSError *error;
    BOOL success = [mobileNettskjema forceRetryAllSubmissionsAndReturnError:&error :^{ }];
    if (!success) {
        reject(@"force_retry_failed", @"Force retry all submissions failed", error);
    } else {
        resolve(nil);
    }
}


RCT_EXPORT_METHOD(setAutoSubmissionsPreference:(NSString *)value resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema setAutoSubmissionsPreference:value];
    resolve(nil);
}

RCT_EXPORT_METHOD(stateOfSubmissions:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSError *error;
    NSArray *result = [mobileNettskjema submissionStateStringsAndReturnError:&error];
    if (result == nil) {
        reject(@"state_of_submissions_failed", @"Getting submission states failed", error);
    } else {
        resolve(result);
    }
}

@end
