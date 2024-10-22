package hw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <b>RatPoly</b> represents an immutable single-variate polynomial expression.
 * RatPolys are sums of terms with rational coefficients and non-negative exponents.
 * <p>
 *
 * Examples of RatPolys include "0", "x-10", and "x^3-2*x^2+5/3*x+3", and "NaN".
 */
// See RatNum's documentation for a definition of "immutable".
public final class RatPoly {

    /** Holds all the RatNum coefficients in this RatPoly */
    private final RatNum[] coeffs;
    /** Holds the degree of this RatPoly */
    private final int degree;

    // Abstraction Function:
    // RatPoly, p, represents the polynomial equal to the sum of the terms:
    // sum (0 <= i < length(p)): p.coeffs[i]*x^i
    // If there are no coefficients, p represents the "0" polynomial.
    //
    // Representation Invariant for every RatPoly p:
    // coeffs != null &&
    // foreach i, 0<=i<coeffs.length: coeffs[i] != null &&
    // if p is the "0" polynomial
    //      coeffs is an empty array and degree = 0
    // else
    //      degree = coeffs.length - 1 &&
    //      coeffs[degree] != 0
    //
    // In other words:
    // * The coeffs field always points to some usable object array.
    // * No coefficient is null.
    // * The degree field is the highest power and should be one less 
    // * than the size of the coeffs array. 
    // * The coefficient of the highest-power term must be non-zero.
    
    
    /** A constant holding a Not-a-Number (NaN) value of type RatPoly */
    public static final RatPoly NaN = new RatPoly(new RatNum[] { RatNum.NaN });

    /** A constant holding a zero value of type RatPoly */
    public static final RatPoly ZERO = new RatPoly();

    /**
     * @effects Constructs a new Poly with value "0".
     */
    public RatPoly() {
        coeffs = new RatNum[0];
        degree = 0;
        checkRep();
    }

    
    /**
     * @param c The constant in the term which the new RatPoly equals.
     * @param e The exponent in the term which the new RatPoly equals.
     * @requires e >= 0
     * @effects Constructs a new RatPoly equal to "c*x^e". If c is zero, constructs
     *          a "0" polynomial.
     */
    public RatPoly(int c, int e) {
    	if (e < 0) {
            throw new IllegalArgumentException("Exponent must be non-negative");
        }
    	if (c == 0) {
            // If the coefficient is zero, construct a "0" polynomial
            coeffs = new RatNum[0];
            degree = 0;
    	}
    	else {
    		coeffs = new RatNum[e+1];
    		for (int i = 0; i < e; i++) {
                coeffs[i] = RatNum.ZERO;
            }
    		coeffs[e] = new RatNum(c);
    		degree = e;
    	}
    	checkRep();

    }

    
    /**
     * @param coeffs An array of coefficients to be contained in the new RatPoly.
     * @requires 'coeffs' is non-empty and it satisfies clauses given in rep. invariant
     * @effects Constructs a new Poly using 'coeffs' as part of the representation.
     */
    public RatPoly(RatNum[] coeffs) {
        this.coeffs = coeffs;
        this.degree = coeffs.length - 1;
        // (argument satisfies the clauses of the rep. invariant
        checkRep();
    }

    /**
     * Returns the degree of this RatPoly.
     *
     * @requires !this.isNaN()
     * @return the largest exponent with a non-zero coefficient, or 0 if this is
     *         "0".
     */
    public int degree() {
  
    	return degree;
    }

    /**
     * Gets the coefficient of the term of power 'pow'
     *
     * @param pow The power for which to find the corresponding coefficient.
     * @requires !this.isNaN()
     * @return the RatNum that is the coefficient of the term of power 'pow'.
     *         "0" if this is "0" || pow < 0 || pow >= coeffs.size 
     */
    public RatNum getCoeff(int pow) {
    	if (pow < 0 || pow >= coeffs.length) {
            return RatNum.ZERO;
        }
        return this.coeffs[pow];
    }

    /**
     * Returns true if this RatPoly is not-a-number.
     *
     * @return true if and only if this has some coefficient = "NaN".
     */
    public boolean isNaN() {
    	for (RatNum coeff : this.coeffs) {
            if (coeff.isNaN()) {
                return true; // If any coefficient is NaN, return true
            }
        }
        return false;  	
    }

        
    /**
     * Scales coefficients within 'arr' by 'scalar' (helper procedure).
     *
     * @param arr The RatNums to be scaled.
     * @param scalar the value by which to scale coefficients in arr.
     * @requires arr, scalar != null
     * @modifies arr
     * @effects Forall i s.t. 0 <= i < arr.length, arr_post[i] = arr_pre[i]*scalar
     *          
     */
    private static void scaleCoeff(RatNum[] arr, RatNum scalar) {
    	for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].mul(scalar); // Multiply each coefficient by scalar
        }
    }


    /**
     * Return the additive inverse of this RatPoly.
     *
     * @return a RatPoly equal to "0 - this"; if this.isNaN(), returns some r
     *         such that r.isNaN()
     */
    public RatPoly negate() {
    	if (coeffs == ZERO.coeffs) {
    		return ZERO;
    	}
    	RatNum[] negated_coeffs = new RatNum[coeffs.length];
    	for (int i = 0; i < coeffs.length; i++) {
    		negated_coeffs[i] = coeffs[i].negate();
    	}
    	RatPoly negated_coeffs_poly = new RatPoly(negated_coeffs);
    	
    	return negated_coeffs_poly;
    	
    }


	/**
     * Addition operation.
     *
     * @param p The other value to be added.
     * @requires p != null
     * @return a RatPoly, r, such that r = "this + p"; if this.isNaN() or
     *         p.isNaN(), returns some r such that r.isNaN()
     */
    public RatPoly add(RatPoly p) {
    	int number = 0; 
    	
    	if (p.coeffs.length < coeffs.length) {
    		number = coeffs.length;
    	} 
    	else if (p.coeffs.length  == coeffs.length) {
    		number = coeffs.length;
    	}
    	else {
    		number = p.coeffs.length;
    	}
    	
    	if (p.coeffs == ZERO.coeffs && coeffs == ZERO.coeffs) {
    		return ZERO;
    	}
    	else if (p.isNaN() == true) {
    		return NaN;
    	}
    	boolean counter = true;
    	RatNum[] added = new RatNum[number];
    	for (int i = 0; i < number; i++) {
    		
    		RatNum num1 =  new RatNum(0);
    		if (i < p.coeffs.length) {
    			num1 = p.coeffs[i];
    		}
    		RatNum num2 =  new RatNum(0);
    		if (i < coeffs.length) {
    			num2 = coeffs[i];
    		}
    		
    		added[i] = num1.add(num2);
    		if (!(added[i].equals(RatNum.ZERO))) {
    			counter = false;
    		}
    	}
    	
    	if (counter) {
            return RatPoly.ZERO; 
        }
    	
    	RatPoly added_final = new RatPoly(added);
    	checkRep();
    	return added_final;
    }
    
    /**
     * Subtraction operation.
     *
     * @param p The value to be subtracted.
     * @requires p != null
     * @return a RatPoly, r, such that r = "this - p"; if this.isNaN() or
     *         p.isNaN(), returns some r such that r.isNaN()
     */
    public RatPoly sub(RatPoly p) {

    	int number = 0; 
    	
    	if (p.coeffs.length < coeffs.length) {
    		number = coeffs.length;
    	} 
    	else if (p.coeffs.length  == coeffs.length) {
    		number = coeffs.length;
    	}
    	else {
    		number = p.coeffs.length;
    	}
    	
    	if (p.coeffs == ZERO.coeffs && coeffs == ZERO.coeffs) {
    		return ZERO;
    	}
    	else if (p.isNaN() == true) {
    		return NaN;
    	}

    	
    	RatNum[] added = new RatNum[number];
    	for (int i = 0; i < number; i++) {
    		
    		RatNum num1 =  new RatNum(0);
    		if (i < p.coeffs.length) {
    			num1 = p.coeffs[i];
    		}
    		RatNum num2 =  new RatNum(0);
    		if (i < coeffs.length) {
    			num2 = coeffs[i];
    		}
    		
    		added[i] = num2.sub(num1);
    	}
    	RatPoly added_final = new RatPoly(added);
    	checkRep();
    	return added_final;
    }

    /**
     * Multiplication operation.
     *
     * @param p The other value to be multiplied.
     * @requires p != null
     * @return a RatPoly, r, such that r = "this * p"; if this.isNaN() or
     *         p.isNaN(), returns some r such that r.isNaN()
     */
    public RatPoly mul(RatPoly p) {
    	if (this.isNaN() || p.isNaN() ) {
    		return NaN;
    	}
        if (this.equals(RatPoly.ZERO) || p.equals(RatPoly.ZERO)) {
            return RatPoly.ZERO;
        }
    	int final_arr_degree = p.degree + this.degree;
    	
    	
    	RatNum[] ratNumArray = new RatNum[final_arr_degree + 1];
    	
    	for (int i = 0; i < final_arr_degree + 1; i++) {
            ratNumArray[i] = RatNum.ZERO;
        }
    	
    	for (int i = 0; i < p.coeffs.length; i++ ) {
    		if (p.coeffs[i] == RatNum.ZERO ) {
    			continue;
    		}
    		for (int j = 0; j < coeffs.length; j++) {
    			if (coeffs[j] == RatNum.ZERO) {
    				continue;
    			}
    			if (ratNumArray[i+j] == RatNum.ZERO) {
    				ratNumArray[i+j] = p.coeffs[i].mul(coeffs[j]);
    			}
    			
    			else {

    			ratNumArray[i+j] = ratNumArray[i+j].add(p.coeffs[i].mul(coeffs[j]));
    			
    			}
    			
    			
    		}
    	}
    	
    	RatPoly final_arr = new RatPoly(ratNumArray);
    	checkRep();
    	return final_arr;
    	
    	
    	
    }

    /**
     * Polynomial Division operation.
     *
     * @param p the divisor
     * @requires p != null
     * @requires p.degree <= this.degree
     * @return a RatPoly, q, such that this = q*p + r; if this.isNaN() or
     *         p.isNaN(), returns some r such that r.isNaN()
     *         return RatPoly.NaN if p is "0"
     *         return "0" polynomial if p.degree > this.degree
     */
    public RatPoly div(RatPoly p) {
    	
    	int du = this.degree;
        int dp = p.degree;
        
        if (this.isNaN() || p.isNaN()) {
            return RatPoly.NaN;
        }

        if (dp > du) {
            return RatPoly.ZERO;
        }
        if (p.equals(RatPoly.ZERO) || this.equals(RatPoly.ZERO)) {
            return RatPoly.NaN;
        }

        if (p.coeffs[0].equals(new RatNum(1)) && p.degree == 0) {
        	return this;
        }

        RatNum[] arr_final = new RatNum[du - dp + 1];
        
        for (int i = 0; i < arr_final.length; i++) {
            arr_final[i] = RatNum.ZERO;
        }
        
        RatNum[] newCoeffs = Arrays.copyOf(this.coeffs, this.coeffs.length);
        for (int i = du; i >= dp; i--) {
            int top_degree = i - dp;
            RatNum topcoeff = newCoeffs[i].div(p.coeffs[dp]);
            arr_final[top_degree] = topcoeff;
            for (int j = 0; j <= p.degree; j++) {
                newCoeffs[i - p.degree + j] = newCoeffs[i - p.degree + j].sub(p.coeffs[j].mul(topcoeff)); 
            }
        }
        checkRep();
        return new RatPoly(arr_final);
    	
    }


    /**
     * Returns the value of this RatPoly, evaluated at d. Evaluate using Horner's
     * rule.
     *
     * @param d The value at which to evaluate this polynomial.
     * @return the value of this polynomial when evaluated at 'd'. For example,
     *         "x+2" evaluated at 3 is 5, and "x^2-x+1" evaluated at 3 is 7. if
     *         (this.isNaN() == true), return Double.NaN
     */
    public double eval(double d) {
        double value = 0;
        for(int i  = 0; i < coeffs.length; i++) {
        	value = value + (Math.pow(d, i) * coeffs[i].doubleValue());
        }
        return value;

    }  
    
    
    /**
     * Return the derivative of this RatPoly.
     *
     * @return a RatPoly, q, such that q = dy/dx, where this == y. In other
     *         words, q is the derivative of this. If this.isNaN(), then return
     *         some q such that q.isNaN()
     *
     * <p>
     * The derivative of a polynomial is the sum of the derivative of each term.
     */
    public RatPoly differentiate() {
    	RatNum[] ratNumArray = new RatNum[this.degree];
    	 if (this.equals(RatPoly.ZERO)) {
             return RatPoly.ZERO;
         }
    	 if (this.isNaN()) {
     		return NaN;
     	}
    	 
    	if(this.degree == 0) {
    		return RatPoly.ZERO;
    	}
    	 
    	for(int i = 0; i < coeffs.length;i++) {
    		RatNum temp = new RatNum(i);
    		if (i == 0 ) {
    			ratNumArray[i] = RatNum.ZERO;
    		}
    		else {
    			ratNumArray[i-1] = coeffs[i].mul(temp);
    		}
    	}
    	RatPoly final_arr = new RatPoly(ratNumArray);
    	checkRep();
    	return final_arr;
    }

    /**
     * Returns the antiderivative of this RatPoly.
     *
     * @param integrationConstant The constant of integration to use when
     *  computing the antiderivative.
     * @requires integrationConstant != null
     * @return a RatPoly, q, such that dq/dx = this and the constant of
     *         integration is "integrationConstant" In other words, q is the
     *         antiderivative of this. If this.isNaN() or
     *         integrationConstant.isNaN(), then return some q such that
     *         q.isNaN()
     *
     * <p>
     * The antiderivative of a polynomial is the sum of the antiderivative of
     * each term plus some constant.
     */
    public RatPoly antiDifferentiate(RatNum integrationConstant) {
    	RatNum[] ratNumArray = new RatNum[this.degree+2];
   	 if (this.equals(RatPoly.ZERO)) {
   		 if (integrationConstant.equals(RatNum.ZERO)) {
             return RatPoly.ZERO;
         }
   		 else {
   			return new RatPoly(new RatNum[] {integrationConstant});
   		 }
   	 }
   	 if (this.isNaN()) {
    		return NaN;
    	}
   	for(int i = 0; i < coeffs.length;i++) {
   		RatNum temp = new RatNum(i+1);
   		if (i == 0 ) {
   			ratNumArray[0] = integrationConstant;
   			ratNumArray[i+1] = coeffs[i];
   		}
   		else {
   			ratNumArray[i+1] = coeffs[i].div(temp);
   			
   		}
   	}
   	RatPoly final_arr = new RatPoly(ratNumArray);
   	checkRep();
   	return final_arr;
    }

    /**
     * Returns the integral of this RatPoly, integrated from lowerBound to
     * upperBound.
     *
     * <p>
     * The Fundamental Theorem of Calculus states that the definite integral of
     * f(x) with bounds a to b is F(b) - F(a) where dF/dx = f(x) NOTE: Remember
     * that the lowerBound can be higher than the upperBound.
     *
     * @param lowerBound The lower bound of integration.
     * @param upperBound The upper bound of integration.
     * @return a double that is the definite integral of this with bounds of
     *         integration between lowerBound and upperBound. If this.isNaN(),
     *         or either lowerBound or upperBound is Double.NaN, return
     *         Double.NaN.
     */
    public double integrate(double lowerBound, double upperBound) {
    	RatNum[] ratNumArray = new RatNum[this.degree+2];
      	 if (this.equals(RatPoly.ZERO)) {
       		return 0;
       	}
      	 else if (this.isNaN()){
      		 return Double.NaN;
      	 }
      	 
      	for(int i = 0; i < coeffs.length;i++) {
      		RatNum temp = new RatNum(i+1);
      		if (i == 0 ) {
      			ratNumArray[0] = RatNum.ZERO;
      			ratNumArray[i+1] = coeffs[i];
      		}
      		else {
      			ratNumArray[i+1] = coeffs[i].div(temp);
      			
      		}
      	}
    	RatPoly final_arr = new RatPoly(ratNumArray);
      	double result_lower = final_arr.eval(lowerBound);
      	double result_upper = final_arr.eval(upperBound);
      	return result_upper - result_lower;
    }

    
    /**
     * Returns a string representation of this RatPoly.
     *
     * @return A String representation of the expression represented by this,
     *         with the terms sorted in order of degree from highest to lowest.
     *         <p>
     *         There is no whitespace in the returned string.
     *         <p>
     *         If the polynomial is itself zero, the returned string will just
     *         be "0".
     *         <p>
     *         If this.isNaN(), then the returned string will be just "NaN"
     *         <p>
     *         The string for a non-zero, non-NaN poly is in the form
     *         "(-)T(+|-)T(+|-)...", where "(-)" refers to a possible minus
     *         sign, if needed, and "(+|-)" refer to either a plus or minus
     *         sign, as needed. For each term, T takes the form "C*x^E" or "C*x"
     *         where C > 0, UNLESS: (1) the exponent E is zero, in which case T
     *         takes the form "C", or (2) the coefficient C is one, in which
     *         case T takes the form "x^E" or "x". In cases were both (1) and
     *         (2) apply, (1) is used.
     *         <p>
     *         Valid example outputs include "x^17-3/2*x^2+1", "-x+1", "-1/2",
     *         and "0".
     *         <p>
     */
    @Override
    public String toString() {
        if (coeffs.length == 0) {
        	return "0";
        }
    	if (isNaN()) {
            return "NaN";
        }
        
        StringBuilder output = new StringBuilder();
        boolean isFirst = true;
        for (int i=coeffs.length-1; i>=0; i--) {
        	// We print nothing for terms with 0 coefficients
        	if (coeffs[i].compareTo(RatNum.ZERO) == 0) continue;
            String term = formatTerm(coeffs[i],i);
        	if (isFirst) {
                isFirst = false;
                output.append(term);
            } 
            else {
            	if (coeffs[i].isNegative()) {
            		output.append(term);
                } else {
                    output.append("+" + term);
                }
            }
        }
        
        return output.toString();
    }
    /**
     * Helper function. Formats the term with coefficient c and exponent 
     * e according to the toString rules.
     * 
     * @param c is the coefficient of the term, e is the exponent
     * @requires c non-null and e >= 0
     * @returns a new string representing the formatted term
     * 
     */
    private static String formatTerm(RatNum c, int e) {
    	StringBuilder output = new StringBuilder();
    	if (e == 0) {
    		// if exponent is 0, add the string representation of coefficient
    		output.append(c.toString());
    	}
    	else if (c.compareTo(new RatNum(1)) == 0) {
    		// if e != 0 and coefficient is 1, skip coefficient
    		output.append("x");
    	}
    	else if (c.compareTo(new RatNum(-1)) == 0) {
    		// if e != 0 and coefficient is -1, skip coefficient
    		output.append("-x");
    	}
    	else {
    		// if e != 0 and |coefficient| != 1, add as expected
    		output.append(c.toString());
    		output.append("*x");
    	}
    	if (e>1) output.append("^"+e);
    	
    	return output.toString();
    }
    
    /**
     * Builds a new RatPoly, given a descriptive String.
     *
     * @param polyStr A string of the format described in the @requires clause.
     * @requires 'polyStr' is an instance of a string with no spaces that
     *           expresses a poly in the form defined in the toString() method.
     *           <p>
     *
     * Valid inputs include "0", "x-10", and "x^3-2*x^2+5/3*x+3", and "NaN".
     *
     * @return a RatPoly p such that p.toString() = polyStr
     */
    public static RatPoly valueOf(String polyStr) {

    	final class RatTerm {
    	   RatNum coefficient;
    	   int exponent;
     	}
    	if (polyStr.equals("0")) return RatPoly.ZERO;
    	if (polyStr.equals("NaN")) return RatPoly.NaN;
    	
        List<RatTerm> parsedTerms = new ArrayList<RatTerm>();

        // First we decompose the polyStr into its component terms;
        // third arg orders "+" and "-" to be returned as tokens.
        StringTokenizer termStrings = new StringTokenizer(polyStr, "+-", true);
        int degree = -1;
        boolean nextTermIsNegative = false;
        while (termStrings.hasMoreTokens()) {
            String termToken = termStrings.nextToken();
            
            if (termToken.equals("-")) {
                nextTermIsNegative = true;
            } else if (termToken.equals("+")) {
                nextTermIsNegative = false;
            } else {
                // Not "+" or "-"; must be a term
            	RatTerm term = new RatTerm();
                // If termToken has "x", decompose into coeff and exponent
            	// otherwise, treat as just a coefficient
                if (termToken.contains("x")) { 
                    int xIndex = termToken.indexOf("x");
                    String c = termToken.substring(0,xIndex); // the coefficient
                    if (c.equals("")) term.coefficient = new RatNum(1); 
                    else term.coefficient = RatNum.valueOf(c.substring(0,c.length()-1));
                    String e = termToken.substring(xIndex+1); // the exponent
                    if (e.equals("")) term.exponent = 1;
                    else term.exponent = Integer.parseInt(e.substring(1));
                }
                else { // Token is the 0-power term
                	term.coefficient = RatNum.valueOf(termToken);
                	term.exponent = 0;
                }
                // Skip the terms with 0-coefficients
                if (term.coefficient.compareTo(RatNum.ZERO) == 0) continue;
                // Record the degree of the polynomial. Test succeeds only for first (highest-power) term.
                if (term.exponent > degree) degree = term.exponent;
                // at this point, coeff and expt are initialized.
                // Need to fix coeff if it was preceeded by a '-'
                if (nextTermIsNegative) {
                	term.coefficient = term.coefficient.negate();
                }

                // accumulate terms of polynomial in 'parsedTerms'
                parsedTerms.add(term);
            }
        }
        // If degree = -1, then all terms had 0 coefficients. Return the 0 poly
        if (degree == -1) return RatPoly.ZERO;
        // Otherwise, construct the array of coefficients from the parsedTerms list
        RatNum[] coefficients = new RatNum[degree+1];
        // initializes the coefficients to 0
        for (int i=0; i<coefficients.length; i++) {
        	coefficients[i] = RatNum.ZERO;
        }
        for (int i=0; i<parsedTerms.size(); i++) {
        	RatTerm term = parsedTerms.get(i);
        	coefficients[term.exponent] = term.coefficient;
        }
        
        return new RatPoly(coefficients);
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    public int hashCode() {
        // all instances that are NaN must return the same hashcode;
        if (this.isNaN()) {
            return 0;
        }
        return Arrays.hashCode(coeffs);
    }

    /**
     * Standard equality operation.
     *
     * @param obj The object to be compared for equality.
     * @return true if and only if 'obj' is an instance of a RatPoly and 'this'
     *         and 'obj' represent the same rational polynomial. Note that all
     *         NaN RatPolys are equal.
     */
    @Override
    public boolean equals(/*@Nullable*/ Object obj) {
        if (obj instanceof RatPoly) {
            RatPoly rp = (RatPoly) obj;

            // special case: check if both are NaN
            if (this.isNaN() && rp.isNaN()) {
                return true;
            } else {
                return Arrays.equals(coeffs,rp.coeffs);
            }
        } else {
            return false;
        }
    }

    /**
     * Checks that the representation invariant holds (if any).
     */
    // Throws a RuntimeException if the rep invariant is violated.
    private void checkRep() throws RuntimeException {
        if (coeffs == null) {
            throw new RuntimeException("coeffs == null");
        }
        if (coeffs.length == 0) {
        	if (degree != 0) {
        		throw new RuntimeException("Degree of the 0 polynomial is not 0");
        	}
        }
        else {
        	if (coeffs.length-1 != degree) {
        		throw new RuntimeException("degree != coeffs.length-1");    
        	}
 		    for (int i = 0; i < coeffs.length; i++) {
		        if (coeffs[i] == null) {
		        	throw new RuntimeException("coefficient "+i+" is null");
		        }
 		    }
		    if (coeffs[degree].compareTo(RatNum.ZERO) == 0) {
		    	throw new RuntimeException("coeffs[degree] is 0");
		    }
        }
    }
}

