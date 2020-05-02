package work.lclpnet.core.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public abstract class CommandBase {

	protected String name;
	protected List<String> aliases = null;
	
	public CommandBase(String name) {
		this.name = Objects.requireNonNull(name);
	}
	
	protected abstract LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder);

	public List<String> getAliases() {
		return aliases;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	
	public void addAlias(String alias) {
		if(alias == null) return;
		if(aliases == null) aliases = new ArrayList<>();
		aliases.add(alias);
	}
	
	public void register(final CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(transform(Commands.literal(name)));
		
		/* Aliases */
		if(aliases != null) aliases.forEach(alias -> dispatcher.register(transform(Commands.literal(alias))));
	}
	
}
