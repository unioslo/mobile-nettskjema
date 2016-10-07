#import "MobileNettskjema.h"
#import "RNEventSink.h"
@import MobileNettskjema;


@implementation MobileNettskjema

{
    MobileNettskjemaObjC *mobileNettskjema;
}

- (id)init {
    self = [super init];
    if (self) {
        LibraryCacheStorageDirectory *directory = [[LibraryCacheStorageDirectory alloc] init];
        RNEventSink *eventSink = [[RNEventSink alloc] init];
        mobileNettskjema = [[MobileNettskjemaObjC alloc] initWithStorageDirectory:directory eventSink:eventSink];
    }
    return self;
}

RCT_EXPORT_MODULE();
RCT_EXPORT_METHOD(addToSubmissionQueue:(NSDictionary *)submission resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema addToSubmissionQueue:submission error:nil onFirstProcessingComplete:^{ }];
    resolve(nil); // FIXME
}
RCT_EXPORT_METHOD(stateOfSubmissions:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    resolve(nil); // FIXME
}
RCT_EXPORT_METHOD(setAutoSubmissionsPreference:(NSString *)value resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    [mobileNettskjema setAutoSubmissionsPreference:value];
    resolve(nil); // FIXME
}

@end
