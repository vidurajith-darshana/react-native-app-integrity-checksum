
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNAppIntegrityChecksumSpec.h"

@interface AppIntegrityChecksum : NSObject <NativeAppIntegrityChecksumSpec>
#else
#import <React/RCTBridgeModule.h>

@interface AppIntegrityChecksum : NSObject <RCTBridgeModule>
#endif

@end
