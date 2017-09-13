@import MobileNettskjemaIOS;
#import <React/RCTEventEmitter.h>


@interface JSEventSink: NSObject <EventSink> {
    RCTEventEmitter *emitter;
}
- (id)initWithEmitter:(RCTEventEmitter*)emitter;
- (void)putWithEvent:(id<Event>)event;
@end
