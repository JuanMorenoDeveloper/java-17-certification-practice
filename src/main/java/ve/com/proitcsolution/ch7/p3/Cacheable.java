package ve.com.proitcsolution.ch7.p3;

sealed interface Cacheable permits Value, Result{
    default void clear(){ System.out.println("clearing cache..."); }
}