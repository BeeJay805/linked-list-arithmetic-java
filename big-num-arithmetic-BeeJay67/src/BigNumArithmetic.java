import static java.lang.Integer.parseInt;

public class BigNumArithmetic {

    /**
     * The entry point of the program.
     * @param args Command line arguments. Should have exactly one argument: a file name.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException(
                    "Expected exactly 1 argument: a file name.");
        }
        String filePath = args[0];
        FileProcessor.processFile(filePath);
    }
    public static void solveAndPrint(String line){
        char op;
        if (line.contains("+")) op = '+';
        else if (line.contains("*")) op = '*';
        else if (line.contains("^")) op = '^';
        else throw new IllegalArgumentException("Invalid Expression");
        //make linked lists
        String[] parts = line.split("\\" + op);
        DigitList left = new DigitList(parts[0]);
        DigitList right = new DigitList(parts[1]);
        //Start arithmetic
        if (op == '+'){
            System.out.println(left + " + " + right + " = " + add(left, right));
        } else if (op == '*'){
            System.out.println(left + " * " + right + " = " + multiply(left, right));
        } else {
            System.out.println(left + " ^ " + right + " = " + power(left, parseInt(parts[1])));
        }
    }
    /*
     * BEFORE:
     *   Both numbers are stored LSB-first (ones place at the head).
     *   Example: 123 is stored as 3 -> 2 -> 1
     *
     * AFTER:
     *   The two numbers are added digit-by-digit from least-significant
     *   to most-significant, carrying as needed.
     *   Missing digits are treated as 0.
     *
     *   Example:
     *     add(123, 89)
     *     Step-by-step:
     *       3 + 9 = 12  -> write 2, carry 1
     *       2 + 8 = 10 + carry 1 -> write 1, carry 1
     *       1 + 0 = 1  + carry 1 -> write 2
     *
     *     Internal result (LSB-first): 2 -> 1 -> 2
     *     Printed result: 212
     */
    public static DigitList add(DigitList left, DigitList right){
        //p is left number
        //q is right number
        DigitNode p = left.getHead();
        DigitNode q = right.getHead();

        int carry = 0;

        DigitList result = new DigitList();
        while (p != null || q != null || carry != 0) {
            //if digit is null then set it = 0
            int leftDig = (p != null) ? p.digit : 0;
            int rightDig = (q != null) ? q.digit : 0;
            //add digits + carry
            int sum = leftDig + rightDig + carry;
            //This is the digit to be added to list
            int resultDig = sum % 10;
            //Set the carry
            carry = sum / 10;
            //Add digit to list
            result.appendDigit(resultDig);
            //Move pointer forward if .next exists
            if (p != null) {
                p = p.next;
            }
            if (q != null) {
                q = q.next;
            }
        }
        return result;
    }
    public static DigitList multiply(DigitList left, DigitList right) {
        DigitNode q = right.getHead(); // LSB -> MSB
        int position = 0;
        DigitList result = new DigitList();
        while(q != null){
            int digit = q.digit;
            DigitList partial = multiplyByDigit(left, digit);
            DigitList shifted = shiftLeft(partial, position);
            result = add(result, shifted);
            position++;
            q = q.next;
        }
        return result;
    }
/*
 * BEFORE:
 *   The number is stored LSB-first (ones place at the head).
 *   Example: 123 is stored as 3 -> 2 -> 1
 *
 * AFTER:
 *   Each digit is multiplied by a single digit (0–9), carrying as needed.
 *   The result is built LSB-first by appending each computed digit.
 *
 *   Example:
 *     multiplyByDigit(123, 5)
 *     Step-by-step:
 *       3 * 5 = 15  -> write 5, carry 1
 *       2 * 5 = 10 + carry 1 -> write 1, carry 1
 *       1 * 5 = 5  + carry 1 -> write 6
 *
 *     Internal result (LSB-first): 5 -> 1 -> 6
 *     Printed result: 615
*/
    public static DigitList multiplyByDigit(DigitList number, int digit) {
        // fast exits
        if (digit == 0) return new DigitList("0"); //if multiply by 0 return 0
        if (digit == 1) return new DigitList(number.toString()); //if multiply ny 1 return original value
        DigitList result = new DigitList(); //create blank linked list for return
        int carry = 0;
        DigitNode cur = number.getHead();
        while (cur != null) { //goes until no more digits
            int product = cur.digit * digit + carry;//computes and adds carry
            result.appendDigit(product % 10);//adds digit to result
            carry = product / 10;//computes carry
            cur = cur.next; //moves to next digit
        }
        while (carry > 0) { //adds the rest of the carry digits
            result.appendDigit(carry % 10);
            carry /= 10;
        }

        return result;
    }
    /*
     * BEFORE:
     *   The number is stored LSB-first (ones place at the head).
     *   Example: 123 is stored as 3 -> 2 -> 1
     *
     * AFTER:
     *   The number is multiplied by 10^position by shifting digits left.
     *   This is done by appending 'position' zeros first, then appending
     *   the original digits in the same order.
     *
     *   Example:
     *     shiftLeft(123, 2)
     *     BEFORE  3 -> 2 -> 1
     *     AFTER   0 -> 0 -> 3 -> 2 -> 1
     *     Printed result: 12300
     */
    public static DigitList shiftLeft(DigitList number, int position) {
        DigitList result = new DigitList(); //blank result linked list
        for (int i = 0; i < position; i++) {
            result.appendDigit(0); //shifts over number by magnitude of 10 for each position
        }
        for (DigitNode cur = number.getHead(); cur != null; cur = cur.next) {
            result.appendDigit(cur.digit);
        }
        return result;
    }
    /*
     * BEFORE:
     *  The base is stored as a LSB-first (ones place at the head)
     *  Example: 678 is stored as 8 -> 7 -> 6
     *  The exponent is stored as an integer
     *
     * AFTER:
     *  After a few checks for easy solutions, the function uses a formula
     *  where it squares half of each exponent if the exponent is even
     *  If it is odd, it returns the same value multiplied by the base again
     *  This is done recursively where the exponent is halved until it reaches
     *  the base case of = 0 or = 1
     *
     * Example:
     *  power(100, 2)
     *  BEFORE 0 -> 0 -> 1
     *  AFTER 0 -> 0 -> 0 -> 0 -> 0 -> 1
     *
     */
    public static DigitList power(DigitList base, int exponent) {
        if (exponent < 0) throw new IllegalArgumentException("Negative exponent");
        if (exponent == 0) return new DigitList("1");
        if (exponent == 1) return new DigitList(base.toString());
        DigitList half = power(base, exponent / 2);
        DigitList squared = multiply(half, half);
        if (exponent%2==0){
            return squared;
        } else {
            return multiply(squared, base);
        }
    }
}

