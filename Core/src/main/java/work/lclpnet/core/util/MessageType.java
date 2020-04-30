package work.lclpnet.core.util;

import java.util.function.Function;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class MessageType {

	public static final MessageType INFO = new MessageType(c -> c.applyTextStyle(TextFormatting.GRAY)),
			ERROR = new MessageType(c -> c.applyTextStyle(TextFormatting.RED)),
			SUCCESS = new MessageType(c -> c.applyTextStyle(TextFormatting.GREEN)),
			OTHER = new MessageType(c -> c.applyTextStyle(TextFormatting.AQUA));
	
	private Function<ITextComponent, ITextComponent> apply;
	
	public MessageType(Function<ITextComponent, ITextComponent> apply) {
		this.apply = apply;
	}
	
	public Function<ITextComponent, ITextComponent> getApply() {
		return apply;
	}
	
	public void setApply(Function<ITextComponent, ITextComponent> apply) {
		this.apply = apply;
	}
	
	public ITextComponent apply(ITextComponent component) {
		return apply.apply(component);
	}
	
}
