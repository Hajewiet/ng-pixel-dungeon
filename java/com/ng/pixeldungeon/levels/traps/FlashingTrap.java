/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.ng.pixeldungeon.levels.traps;

import com.ng.pixeldungeon.Assets;
import com.ng.pixeldungeon.Dungeon;
import com.ng.pixeldungeon.actors.Actor;
import com.ng.pixeldungeon.actors.Char;
import com.ng.pixeldungeon.actors.buffs.Blindness;
import com.ng.pixeldungeon.actors.buffs.Buff;
import com.ng.pixeldungeon.actors.buffs.Cripple;
import com.ng.pixeldungeon.actors.mobs.Mob;
import com.ng.pixeldungeon.effects.CellEmitter;
import com.ng.pixeldungeon.effects.Speck;
import com.ng.pixeldungeon.scenes.GameScene;
import com.ng.pixeldungeon.sprites.TrapSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class FlashingTrap extends Trap {

	{
		name = "Flashing trap";
		color = TrapSprite.YELLOW;
		shape = TrapSprite.STARS;
	}

	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);

		if (ch != null) {
			int len = Random.Int(5, 10)+Dungeon.depth;
			Buff.prolong( ch, Blindness.class, len );
			Buff.prolong( ch, Cripple.class, len );
			if (ch instanceof Mob) {
				if (((Mob)ch).state == ((Mob)ch).HUNTING) ((Mob)ch).state = ((Mob)ch).WANDERING;
				((Mob)ch).beckon( Dungeon.level.randomDestination() );
			}
			if (ch == Dungeon.hero){
				Sample.INSTANCE.play( Assets.SND_BLAST );
			}
		}

		if (Dungeon.visible[pos]) {
			GameScene.flash(0xFFFFFF);
			CellEmitter.get(pos).burst( Speck.factory(Speck.LIGHT), 4 );
		}
	}

	@Override
	public String desc() {
		return "On activation, this trap will ignite a potent flashing powder stored within, " +
				"temporarily blinding and crippling its victim.";
	}
}
