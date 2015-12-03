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
package com.ng.pixeldungeon.items.quest;

import com.ng.pixeldungeon.Assets;
import com.ng.pixeldungeon.Dungeon;
import com.ng.pixeldungeon.actors.hero.Hero;
import com.ng.pixeldungeon.actors.mobs.NewbornElemental;
import com.ng.pixeldungeon.effects.CellEmitter;
import com.ng.pixeldungeon.effects.particles.ElmoParticle;
import com.ng.pixeldungeon.items.Heap;
import com.ng.pixeldungeon.items.Item;
import com.ng.pixeldungeon.levels.Level;
import com.ng.pixeldungeon.scenes.GameScene;
import com.ng.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;


public class CeremonialCandle extends Item {

	//generated with the wandmaker quest
	public static int ritualPos;

	{
		name = "ceremonial candle";
		image = ItemSpriteSheet.CANDLE;

		unique = true;
		stackable = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public void doDrop(Hero hero) {
		super.doDrop(hero);
		checkCandles();
	}

	@Override
	protected void onThrow(int cell) {
		super.onThrow(cell);
		checkCandles();
	}

	private static void checkCandles(){
		Heap heapTop = Dungeon.level.heaps.get(ritualPos - Level.WIDTH);
		Heap heapRight = Dungeon.level.heaps.get(ritualPos + 1);
		Heap heapBottom = Dungeon.level.heaps.get(ritualPos + Level.WIDTH);
		Heap heapLeft = Dungeon.level.heaps.get(ritualPos - 1);

		if (heapTop != null &&
				heapRight != null &&
				heapBottom != null &&
				heapLeft != null){

			if (heapTop.peek() instanceof CeremonialCandle &&
					heapRight.peek() instanceof CeremonialCandle &&
					heapBottom.peek() instanceof CeremonialCandle &&
					heapLeft.peek() instanceof CeremonialCandle){

				heapTop.pickUp();
				heapRight.pickUp();
				heapBottom.pickUp();
				heapLeft.pickUp();

				NewbornElemental elemental = new NewbornElemental();
				elemental.pos = ritualPos;
				elemental.state = elemental.HUNTING;
				GameScene.add(elemental, 1);

				for (int i : Level.NEIGHBOURS9){
					CellEmitter.get(ritualPos+i).burst(ElmoParticle.FACTORY, 10);
				}
				Sample.INSTANCE.play(Assets.SND_BURNING);
			}
		}

	}

	@Override
	public String info() {
		return
				"A set of candles, melted down and fused together through use.\n\n" +
				"Alone they are worthless, but used with other candles in a pattern, " +
				"they can focus the energy for a summoning ritual.";
	}
}
