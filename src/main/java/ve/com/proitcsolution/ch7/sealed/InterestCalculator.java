package ve.com.proitcsolution.ch7.sealed;

import ve.com.proitcsolution.ch7.package2.FinMath;

public sealed class InterestCalculator permits CompoundIntCalculator, DefaultInterestCalculator, NewInterestCalculator, FinMath {}
