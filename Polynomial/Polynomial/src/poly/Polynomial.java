package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */

	public static Node add(Node poly1, Node poly2) {
		//start of correct code
        Node front = new Node(0, 0, null);
        Node ptr = front;
        Node ptr1 = poly1;
        Node ptr2 = poly2;

		while (ptr1 != null || ptr2 != null) {

        	if (ptr1 == null) {

				ptr.next = new Node(ptr2.term.coeff,ptr2.term.degree,null);

				ptr2 = ptr2.next;
				ptr=ptr.next;
			}

			else if (ptr2 == null) {

				ptr.next = new Node(ptr1.term.coeff,ptr1.term.degree,null);

				ptr1 = ptr1.next;
				ptr=ptr.next;
			}
            else if (ptr1.term.degree > ptr2.term.degree) {
                ptr.next = new Node(ptr2.term.coeff,ptr2.term.degree,null);

                ptr2 = ptr2.next;
                ptr=ptr.next;
            }
            else if (ptr2.term.degree > ptr1.term.degree) {
                ptr.next = new Node(ptr1.term.coeff,ptr1.term.degree,null);

                ptr1 = ptr1.next;
                ptr=ptr.next;
            }
			 else if (ptr1.term.degree == ptr2.term.degree) {
				if(ptr1.term.coeff+ptr2.term.coeff!=0){
					ptr.next = new Node(ptr1.term.coeff + ptr2.term.coeff,ptr1.term.degree,null);
					ptr=ptr.next;
				}
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}


        }
		return front.next;
    }
    //end of correct code

	// wrong code
//		Node ptr1=poly1;
//		Node ptr2=poly2;
//		Node front = new Node(0,0,null);
//		Node ptr = front;
//		if(ptr1==null && ptr2==null){
//		    return front.next;
//        }
//		while (ptr1.next!=null && ptr2.next!=null) {
//			if (ptr1.term.degree>ptr2.term.degree) {
//				ptr.term.coeff=ptr2.term.coeff;
//				ptr.term.degree=ptr2.term.degree;
//				ptr2=ptr2.next;
//
//			}
//			else if (ptr2.term.degree>ptr1.term.degree) {
//				ptr.term.coeff=ptr1.term.coeff;
//				ptr.term.degree=ptr1.term.degree;
//				ptr1=ptr1.next;
//
//			}
//			else {
//				if(ptr1.term.coeff+ptr2.term.coeff!=0){
//			    ptr.term.coeff=ptr1.term.coeff+ptr2.term.coeff;
//				ptr.term.degree=ptr1.term.degree;
//
//				ptr.next=new Node(0,0,null);
//				ptr=ptr.next;
//				}
//                ptr1=ptr1.next;
//                ptr2=ptr2.next;
//			}
//		}
//		if (ptr1.next!=null) {
//			while (ptr1 != null) {
//				Node newnode=new Node(ptr1.term.coeff,ptr1.term.degree,null);
//				ptr.next=newnode;
//				ptr1 = ptr1.next;
//			}
//		}
//		if (ptr2.next!=null) {
//			while (ptr2 != null) {
//				Node newnode=new Node(ptr2.term.coeff,ptr2.term.degree,null);
//				ptr.next=newnode;
//				ptr2 = ptr2.next;
//			}
//		}
//
//		//deleteBack(front);
//		return front.next;


	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		//start of correct code
		Node ptr1=poly1;
		Node ptr2=poly2;
		Node newnode=new Node(0,0,null);
		Node ptr=newnode;
		Node fina = new Node (0,0,null);
		while(ptr1!=null) {
		    while (ptr2!=null) {
                ptr.next = new Node(ptr1.term.coeff * ptr2.term.coeff, ptr1.term.degree + ptr2.term.degree, null);
                ptr = ptr.next;
                ptr2 = ptr2.next;

            }
		    fina.next = add(fina.next, newnode.next);
            newnode.next=null;
            ptr=newnode;
            ptr2=poly2;
            ptr1=ptr1.next;
        }
		return fina.next;

	}
	//end of correct code
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		float y=0.0f;
		float sum=0.0f;
		Node ptr=poly;
		while (ptr!=null) {
			y=(float)((Math.pow(x,ptr.term.degree))*ptr.term.coeff);
			ptr=ptr.next;
			sum=sum+y;
		}
		return sum;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}

	private static void deleteBack(Node front) {
		Node prev = null;
		Node curr = front;
		while (curr.next != null) {
			prev = curr;
			curr = curr.next;
		}
		prev.next=null;
	}

	public static Node deleteFront(Node front) {
		if (front == null) {
			return null;
		}
		return front.next;
	}
	public static Node removeDuplicates(Node front) {
	    Node ptr1=front;
	    Node ptr2=front.next;
	    Node newnode=new Node (0,0,null);
	    Node ptr=newnode;
	    while (ptr1!=null) {
	        while (ptr2.next!=null) {
	            if (ptr1.term.degree==ptr2.term.degree) {
	                ptr.term.coeff=ptr1.term.coeff+ptr2.term.coeff;
	                ptr.term.degree=ptr1.term.degree;
	                ptr.next=new Node(0,0,null);
	                ptr=ptr.next;
	                ptr2=ptr2.next;
                }
	            ptr2=ptr2.next;
            }
	        ptr1=ptr1.next;
        }
	    return newnode;
        }

    }



