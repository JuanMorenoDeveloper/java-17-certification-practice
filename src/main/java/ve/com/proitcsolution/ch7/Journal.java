package ve.com.proitcsolution.ch7;

public record Journal(int id, String... values){ } //valid because there is only one varargs component
