package certidaoautomatica;

import java.util.Objects;

public class Process {
    private String number;
    private int pgNum;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPgNum() {
        return pgNum;
    }

    public void setPgNum(int pgNum) {
        this.pgNum = pgNum;
    }
    
    @Override
    public String toString() {
        return this.getNumber() + " - Páginas: " + this.getPgNum();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Process other = (Process) obj;
        if (!Objects.equals(this.number, other.number)) {
            return false;
        }
        return true;
    }
    
}
