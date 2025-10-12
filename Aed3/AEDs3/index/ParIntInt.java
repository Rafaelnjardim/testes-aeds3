package index;

import java.io.*;
import base.RegistroArvoreBMais;

public class ParIntInt implements RegistroArvoreBMais<ParIntInt> {
    private int num1;
    private int num2;

    public ParIntInt() { this(-1, -1); }
    public ParIntInt(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public int getNum1() { return num1; }
    public int getNum2() { return num2; }
    public void setNum1(int num1) { this.num1 = num1; }
    public void setNum2(int num2) { this.num2 = num2; }

    @Override
    public int compareTo(ParIntInt o) {
        if (this.num1 != o.num1) return this.num1 - o.num1;
        return this.num2 - o.num2;
    }

    @Override
    public short size() { return 8; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(num1);
        dos.writeInt(num2);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        num1 = dis.readInt();
        num2 = dis.readInt();
    }

    @Override
    public ParIntInt clone() {
        return new ParIntInt(this.num1, this.num2);
    }
}

