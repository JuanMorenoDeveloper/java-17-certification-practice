package ve.com.proitcsolution.ch7.p3;

abstract non-sealed interface Value extends Cacheable{ }
sealed abstract class Result implements Cacheable permits IntResult{ }
final class IntResult extends Result{ }