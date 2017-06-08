@import MobileNettskjemaIOS;
#import "RCTEventEmitter.h"


@interface JSEventSink: NSObject <EventSink> {
    RCTEventEmitter *emitter;
}
- (id)initWithEmitter:(RCTEventEmitter*)emitter;
- (void)putWithEvent:(id<Event>)event;
@end
