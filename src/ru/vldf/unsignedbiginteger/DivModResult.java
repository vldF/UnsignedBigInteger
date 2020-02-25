package ru.vldf.unsignedbiginteger;

public class DivModResult {
    UnsignedBigInteger div;
    UnsignedBigInteger mod;
    DivModResult(UnsignedBigInteger div, UnsignedBigInteger mod){
        this.div = div;
        this.mod = mod;
    }

    /**
     * @return integer part of division of two UnsignedBigInteger
     */
    public UnsignedBigInteger getDiv(){
        return div;
    }

    /**
     * @return modulo of two UnsignedBigInteger
     */
    public UnsignedBigInteger getMod(){
        return mod;
    }
}
