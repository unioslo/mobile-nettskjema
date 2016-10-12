#import <Foundation/Foundation.h>
#import "JSEventSink.h"


@implementation JSEventSink
-(id)initWithEmitter:(RCTEventEmitter *)rctEmitter {
    self = [self init];
    self->emitter = rctEmitter;
    return self;
}
-(void)put:(id<Event>)event {
    [emitter sendEventWithName:event.name body:event.data];
}
@end
