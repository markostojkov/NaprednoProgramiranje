import java.util.*;

class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}

class SuperString {
    private LinkedList<String> stringLinkedList;
    private Stack<Boolean> lastAddedIndexes;

    public SuperString() {
        this.stringLinkedList = new LinkedList<>();
        this.lastAddedIndexes = new Stack<>();
    }


    public void append(String s) {
        this.lastAddedIndexes.push(false);
        this.stringLinkedList.addLast(s);
    }

    public void insert(String s) {
        this.stringLinkedList.addFirst(s);
        this.lastAddedIndexes.push(true);
    }

    public boolean contains(String s) {
        return this.toString().contains(s);
    }

    public void reverse() {
        LinkedList<String> reversedString = new LinkedList<>();

        for (int j = this.stringLinkedList.size() - 1; j >= 0; j--) {
            StringBuilder stringBuilder = new StringBuilder(this.stringLinkedList.get(j)).reverse();
            reversedString.add(stringBuilder.toString());
        }

        for (int i = 0; i < this.lastAddedIndexes.size(); i++) {
            this.lastAddedIndexes.set(i, !this.lastAddedIndexes.elementAt(i));
        }
        this.stringLinkedList = reversedString;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < this.stringLinkedList.size(); j++) {
            stringBuilder.append(this.stringLinkedList.get(j));
        }

        return stringBuilder.toString();
    }

    public void removeLast(int k) {
        for (int j = k; j > 0; j--) {
            boolean isFirst = this.lastAddedIndexes.pop();
            if (isFirst) {
                this.stringLinkedList.removeFirst();
            } else {
                this.stringLinkedList.removeLast();
            }
        }
    }
}