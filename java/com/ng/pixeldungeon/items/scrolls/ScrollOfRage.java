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
package com.ng.pixeldungeon.items.scrolls;

import com.ng.pixeldungeon.actors.mobs.Mimic;
import com.ng.pixeldungeon.items.Heap;
import com.ng.pixeldungeon.levels.Level;
import com.watabou.noosa.audio.Sample;
import com.ng.pixeldungeon.Assets;
import com.ng.pixeldungeon.Dungeon;
import com.ng.pixeldungeon.actors.buffs.Amok;
import com.ng.pixeldungeon.actors.buffs.Buff;
import com.ng.pixeldungeon.actors.buffs.Invisibility;
import com.ng.pixeldungeon.actors.mobs.Mob;
import com.ng.pixeldungeon.effects.Speck;
import com.ng.pixeldungeon.utils.GLog;

public class ScrollOfRage extends Scroll {

	{
		name = "Scroll of Rage";
		initials = "Ra";
	}
	
	@Override
	protected void doRead() {

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			mob.beckon( curUser.pos );
			if (Level.fieldOfView[mob.pos]) {
				Buff.prolong(mob, Amok.class, 5f);
			}
		}

		for (Heap heap : Dungeon.level.heaps.values()) {
			if (heap.type == Heap.Type.MIMIC) {
				Mimic m = Mimic.spawnAt( heap.pos, heap.items );
				if (m != null) {
					m.beckon( curUser.pos );
					heap.destroy();
				}
			}
		}

		GLog.w( "The scroll emits an enraging roar that echoes throughout the dungeon!" );
		setKnown();
		
		curUser.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
		Sample.INSTANCE.play( Assets.SND_CHALLENGE );
		Invisibility.dispel();
		
		curUser.spendAndNext( TIME_TO_READ );
	}
	
	@Override
	public String desc() {
		return
			"When read aloud, this scroll will unleash a great roar " +
			"that draws all enemies to the reader, and enrages nearby ones.";
	}
}
