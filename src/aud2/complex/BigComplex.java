package aud2.complex;

import java.math.BigDecimal;

public class BigComplex {
    private BigDecimal realPart;
    private BigDecimal imaginaryPart;

    public BigComplex(BigDecimal realPart, BigDecimal imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public BigComplex add(BigComplex complex) {
        return new BigComplex(this.realPart.add(complex.realPart),
                this.imaginaryPart.add(complex.imaginaryPart));
    }

    @Override
    public String toString() {
        return String.format("%d + %dj", realPart.intValue(), imaginaryPart.intValue());
    }

    public static void main(String[] args) {
        BigComplex bigComplex1 = new BigComplex(new BigDecimal(3), new BigDecimal(4));
        BigComplex bigComplex2 = new BigComplex(new BigDecimal(5), new BigDecimal(6));
        System.out.println(bigComplex1.add(bigComplex2));
    }
}
