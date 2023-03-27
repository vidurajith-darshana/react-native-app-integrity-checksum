#import <Foundation/Foundation.h>
#import "AppIntegrityChecksum.h"
#import "AWFileHash.h"
#import <React/RCTLog.h>
#include <sys/stat.h>

@implementation AppIntegrityChecksum
RCT_EXPORT_MODULE()

// Example method
// See // https://reactnative.dev/docs/native-modules-ios
RCT_REMAP_METHOD(getChecksum,
                 getChecksumWithCallback:(RCTResponseSenderBlock)callback)
{
    NSBundle* myBundle = [NSBundle mainBundle];
    NSString* mainJsBundle = [myBundle pathForResource:@"main" ofType:@"jsbundle"];

    NSString *sha512 = [AWFileHash sha512HashOfFileAtPath:mainJsBundle];

    NSLog(@"HASH: %@", sha512);

    callback(@[sha512]);
}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeAppIntegrityChecksumSpecJSI>(params);
}
#endif

@end
