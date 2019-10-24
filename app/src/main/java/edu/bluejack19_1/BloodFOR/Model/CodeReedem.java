package edu.bluejack19_1.BloodFOR.Model;

public class CodeReedem {
    private String codereedem;

    private String redeemType;

    public String getCodereedem() {
        return codereedem;
    }

    public void setCodereedem(String codereedem) {
        this.codereedem = codereedem;
    }


    public String getRedeemType() {
        return redeemType;
    }

    public void setRedeemType(String redeemType) {
        this.redeemType = redeemType;
    }

    public CodeReedem(String codereedem, String redeemType) {
        this.codereedem = codereedem;
        this.redeemType = redeemType;
    }
}
