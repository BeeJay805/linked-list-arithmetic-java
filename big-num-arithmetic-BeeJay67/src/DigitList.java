public class DigitList {

    private DigitNode head;

    public DigitList(String number) {
        head = null;
        //Remove leading 0s
        number = number.replaceFirst("^0+(?!$)", "");

        //Store digits backwards
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = number.charAt(i) - '0';
            addDigit(digit);
        }

    }
    public DigitList() {
        head = null;
    }

    public void addDigit(int digit) {
        // if empty
        if (head == null) {
            head = new DigitNode(digit);
            return;
        }

        DigitNode cur = head;
        while (cur.next != null) cur = cur.next;
        cur.next = new DigitNode(digit);
    }
    public void appendDigit(int digit) {
        DigitNode node = new DigitNode(digit);
        // empty list
        if (head == null) {
            head = node;
            return;
        }
        // walk to tail
        DigitNode cur = head;
        while (cur.next != null) {
            cur = cur.next;
        }
        // append
        cur.next = node;
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        DigitNode current = head;
        while (current != null) {
            sb.append(current.digit);
            current = current.next;
        }
        return sb.reverse().toString();
    }

    public DigitNode getHead() {
        return head;
    }

}

