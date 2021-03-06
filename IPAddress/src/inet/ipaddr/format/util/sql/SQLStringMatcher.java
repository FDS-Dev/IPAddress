package inet.ipaddr.format.util.sql;

import inet.ipaddr.format.IPAddressPart;
import inet.ipaddr.format.util.IPAddressPartConfiguredString;
import inet.ipaddr.format.util.IPAddressPartStringParams;

/**
 * This class is intended to match part of an address when it is written with a given string.
 * 
 * Note that a given address part can be written many ways.  
 * Also note that some of these representations can represent more than one address section.
 * 
 * @author sfoley
 *
 */
public class SQLStringMatcher<T extends IPAddressPart, P extends IPAddressPartStringParams<T>, S extends IPAddressPartConfiguredString<T, P>> {
	protected final S networkString;
	private final boolean isEntireAddress;
	private final IPAddressSQLTranslator translator;

	public SQLStringMatcher(S networkString, boolean isEntireAddress, IPAddressSQLTranslator translator) {
		this.networkString = networkString;
		this.translator = translator;
		this.isEntireAddress = isEntireAddress;
		translator.setNetwork(networkString.getString());
	}
	
	/**
	 * Get an SQL condition to match this address section representation
	 * 
	 * @param builder
	 * @param columnName
	 * @return the condition
	 */
	public StringBuilder getSQLCondition(StringBuilder builder, String columnName) {
		String string = networkString.getString();
		if(isEntireAddress) {
			matchString(builder, columnName, string);
		} else {
			matchSubString(
					builder,
					columnName,
					networkString.getTrailingSegmentSeparator(),
					networkString.getTrailingSeparatorCount() + 1,
					string);
		}
		return builder;
	}
	
	protected void matchString(StringBuilder builder, String expression, String match) {
		translator.matchString(builder, expression, match);
	}
	
	protected void matchSubString(StringBuilder builder, String expression, char separator, int separatorCount, String match) {
		translator.matchSubString(builder, expression, separator, separatorCount, match);
	}
	
	protected void matchSeparatorCount(StringBuilder builder, String expression, char separator, int separatorCount) {
		translator.matchSeparatorCount(builder, expression, separator, separatorCount);
	}
	
	protected void boundSeparatorCount(StringBuilder builder, String expression, char separator, int separatorCount) {
		translator.boundSeparatorCount(builder, expression, separator, separatorCount);
	}

	@Override
	public String toString() {
		return getSQLCondition(new StringBuilder(), "COLUMN").toString();
	}
}