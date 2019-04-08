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
        JSEventSink *eventSink = [[JSEventSink alloc] initWithEmitter:&*self];
        mobileNettskjema = [[ProductionMobileNettskjema alloc] initWithEventSink:eventSink];
    }
    return self;
}

- (NSArray<NSString *>*)supportedEvents {
    return @[ @"submissionStateChanged"];
}


RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(addToSubmissionQueue:(NSDictionary *)submission resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema
     addToSubmissionQueue:submission
     onComplete: ^void (NSString *submission) {
         resolve(submission);
     }
     onFailure: ^void (NSString *reason) {
        reject(@"add_to_queue_failed", reason, nil);
     }];
}

RCT_EXPORT_METHOD(addToSubmissionQueueWithMetadata:(NSDictionary *)submission metadata:(NSString *)metadata resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema
     addToSubmissionQueue:submission
     withMetadata:metadata
     onComplete: ^void (NSString *submission) {
         resolve(submission);
     }
     onFailure: ^void (NSString *reason) {
         reject(@"add_to_queue_with_metadata_failed", reason, nil);
     }];
}

RCT_EXPORT_METHOD(deleteSubmission:(NSString *)submission resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema
     deleteSubmission:submission
     onComplete: ^void (NSString *submission) {
         resolve(submission);
     }
     onFailure: ^void (NSString *reason) {
         reject(@"delete_submission_failed", reason, nil);
     }];
}

RCT_EXPORT_METHOD(deleteSubmissionsIfTooOld:(NSString *)submission resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema
     deleteSubmissionsIfTooOld:^void (NSArray<NSString *> *deletedSubmissions) {
         resolve(deletedSubmissions);
     }
     onFailure:^void (NSString *reason) {
         reject(@"delete_old_submissions_failed", reason, nil);
     }];
}

RCT_EXPORT_METHOD(clearTemporaryFiles:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema clearTemporaryFilesOnComplete:^(void) {
        resolve(nil);
    } onFailure: ^void (NSString *reason) {
        reject(@"clear_temporary_files_failed", reason, nil);
    }];
}

RCT_EXPORT_METHOD(forceRetryAllSubmissions:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema
     forceRetryAllSubmissionsOnComplete:^void (NSArray<NSString *> *results) {
         resolve(results);
     }
     onFailure:^void (NSString *reason) {
         reject(@"force_retry_failed", reason, nil);
     }];
}

RCT_EXPORT_METHOD(retryUploadForFile:(NSString *)submissionId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema
     retryUploadForFile:submissionId
     onComplete: ^void (NSString *submissionResult) {
         resolve(submissionResult);
     }
     onFailure: ^void (NSString *reason) {
         reject(@"retry_submission_failed", reason, nil);
     }];
}


RCT_EXPORT_METHOD(setAutoSubmissionsPreference:(NSString *)value resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema setAutoSubmissionsPreference:value];
    resolve(nil);
}

RCT_EXPORT_METHOD(stateOfSubmissions:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema submissionStateStringsOnComplete:^void (NSArray<NSString *> *results) {
        resolve(results);
    } onFailure: ^void (NSString *reason) {
        reject(@"state_of_submissions_failed", reason, nil);
    }];
}

@end
