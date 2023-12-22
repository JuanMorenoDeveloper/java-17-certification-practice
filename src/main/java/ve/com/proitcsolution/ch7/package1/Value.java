package ve.com.proitcsolution.ch7.package1;

abstract non-sealed interface Value extends Cacheable{ }

abstract sealed class Result implements Cacheable permits IntResult {}

final class IntResult extends Result{ }