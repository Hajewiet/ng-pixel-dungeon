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

import java.util.ArrayList;

import com.ng.pixeldungeon.Dungeon;
import com.ng.pixeldungeon.actors.Actor;
import com.ng.pixeldungeon.actors.mobs.Bestiary;
import com.ng.pixeldungeon.actors.mobs.Mob;
import com.ng.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.ng.pixeldungeon.levels.Level;
import com.ng.pixeldungeon.scenes.GameScene;
import com.ng.pixeldungeon.sprites.TrapSprite;
import com.watabou.utils.Random;

public class SummoningTrap extends Trap {

	private static final float DELAY = 2f;

	{
		name = "Summoning trap";
		color = TrapSprite.TEAL;
		shape = TrapSprite.WAVES;
	}

	@Override
	public void activate() {

		if (Dungeon.bossLevel()) {
			return;
		}

		int nMobs = 1;
		if (Random.Int( 2 ) == 0) {
			nMobs++;
			if (Random.Int( 2 ) == 0) {
				nMobs++;
			}
		}

		ArrayList<Integer> candidates = new ArrayList<>();

		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			int p = pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
				candidates.add( p );
			}
		}

		ArrayList<Integer> respawnPoints = new ArrayList<>();

		while (nMobs > 0 && candidates.size() > 0) {
			int index = Random.index( candidates );

			respawnPoints.add( candidates.remove( index ) );
			nMobs--;
		}

		ArrayList<Mob> mobs = new ArrayList<>();

		for (Integer point : respawnPoints) {
			Mob mob = Bestiary.mob( Dungeon.depth );
			mob.state = mob.WANDERING;
			mob.pos = point;
			GameScene.add( mob, DELAY );
			mobs.add( mob );
		}

		//important to process the visuals and pressing of cells last, so spawned mobs have a chance to occupy cells first
		for (Mob mob : mobs){
			ScrollOfTeleportation.appear(mob, mob.pos);
		}

	}

	@Override
	public String desc() {
		return "Triggering this trap will summon a number of monsters from the surrounding floors to this location.";
	}
}
