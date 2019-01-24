package org.jalgo.module.em.gui.input;

/**
 * Offers a method for converting a string input into a double value. The method
 * is static so it can be used without instancing an object of this class.
 * 
 * @author Tobias Nett
 * 
 */
public class InputStringParser {

	/**
	 * Converts a {@code String} that represents a FlotingPointNumber in the
	 * specified range from {@code min} to {@code max} to double.
	 * <p>
	 * Accepted {@code String} formats are "0,1323", "0.1323", "1/12".
	 * WhiteSpace is ignored.
	 * 
	 * @param string
	 *            String that should be parsed
	 * @param min
	 *            the minimal valid double value
	 * @param max
	 *            the maximal valid double value
	 * @return if input is correct, the corresponding double Value; otherwise an
	 *         exception is raised
	 * 
	 * @throws NumberFormatException
	 *             is raised if the input characters are valid but a parsing
	 *             error occurs
	 * 
	 * @throws IllegalArgumentException
	 *             is raised if the input string is invalid, for example '1/3/9'
	 *             or '1,5.9' or the entered value is out of the specified
	 *             borders of {@code min} and {@code max}.
	 * 
	 */
	public static double parseInputString(final String string, double min,
			double max) throws NumberFormatException, IllegalArgumentException {
		StringBuffer input = new StringBuffer(string);

		int index = 0;
		// Remove WhiteSpace
		while (index < input.length()) {
			if (input.charAt(index) == ' ') {
				input.deleteCharAt(index);
			} else {
				index++;
			}
		}
		index = 0;
		// Replaces ',' with '.'
		while (index < input.length()) {
			if (input.charAt(index) == ',') {
				input.setCharAt(index, '.');
			} else {
				index++;
			}
		}

		// Splits in Case of '/'
		if (input.toString().split("/").length == 2) {
			double numerator = Double
					.parseDouble(input.toString().split("/")[0]);
			double denominator = Double
					.parseDouble(input.toString().split("/")[1]);
			if ((numerator / denominator > max)
					|| (numerator / denominator < min)) {
				throw new IllegalArgumentException();
			}
			return numerator / denominator;

		} else if (input.toString().split("\\.").length <= 2) {
			// "." has to commented because it is used as a regular expression
			// from split method
			double value = Double.parseDouble(input.toString());
			if (value < min || value > max) {
				throw new IllegalArgumentException();
			}
			return value;
		} else {
			throw new IllegalArgumentException();
		}
	}
}
