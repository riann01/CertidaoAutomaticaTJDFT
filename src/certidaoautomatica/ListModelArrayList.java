package certidaoautomatica;

import java.util.List;
import java.util.ArrayList;

public class ListModelArrayList extends javax.swing.AbstractListModel<Process> {

    List<Process> process = new ArrayList<>();

    @Override
    public int getSize() {
        return process.size();
    }

    @Override
    public Process getElementAt(int i) {
        return process.get(i);
    }

    public void addElement(Process el) {
        process.add(el);
    }

    public void addElementAt(int index, Process el) {
        process.add(index, el);
    }

    public void removeElement(Process el) {
        process.remove(el);
    }

    public boolean isEmpty() {
        return process.isEmpty();
    }

    public void removeAllElements() {
        process.clear();
    }

}
