package work.lclpnet.core.util;

import java.lang.reflect.Field;
import java.util.Formatter;
import java.util.regex.Pattern;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ComponentSupplier {

	private String prefix;
	private Pattern FORMATTER_PATTERN;
	
	public ComponentSupplier() {
		this(null);
	}
	
	public ComponentSupplier(String prefix) {
		this.prefix = prefix;
		
		try {
			Field f = Formatter.class.getDeclaredField("fsPattern");
			boolean b = f.isAccessible();
			f.setAccessible(true);
			
			Object pattern = f.get(null);
			
			f.setAccessible(b);
			
			FORMATTER_PATTERN = (Pattern) pattern;
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			FORMATTER_PATTERN = null;
		}
		if(FORMATTER_PATTERN == null) throw new IllegalStateException("Pattern not found");
	}
	
	public ITextComponent getPrefix() {
		return prefix != null ? ComponentHelper.getPrefix(prefix) : new StringTextComponent("");
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public ITextComponent message(String msg, MessageType type) {
		return ComponentHelper.message(prefix, msg, type);
	}
	
	public ITextComponent complexMessage(String msg, TextFormatting tf, Substitute... substitutes) {
		return ComponentHelper.complexMessage(prefix, msg, tf, substitutes);
	}
	
	public ITextComponent complexMessage(String msg, TextFormat tf, Substitute... substitutes) {
		return ComponentHelper.complexMessage(prefix, msg, tf, substitutes);
	}
	
	public static class TextFormat {
		protected TextFormatting[] format;
		
		public TextFormat(TextFormatting... format) {
			this.format = format;
		}
		
		public ITextComponent apply(StringTextComponent component) {
			return component.applyTextStyles(format);
		}

		public TextFormatting[] getFormat() {
			return format;
		}
		
		public void setFormat(TextFormatting[] format) {
			this.format = format;
		}
		
	}
	
	public static class Substitute extends TextFormat{
		private Object obj;
		
		public Substitute(Object obj, TextFormatting... format) {
			super(format);
			this.obj = obj;
		}
		
		public Object getObj() {
			return obj;
		}
		
		public void setObj(Object obj) {
			this.obj = obj;
		}
		
	}
	
}
