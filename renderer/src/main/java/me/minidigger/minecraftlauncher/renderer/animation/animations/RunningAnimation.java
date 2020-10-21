/*
 * MIT License
 *
 * Copyright (c) 2018 Ammar Ahmad
 * Copyright (c) 2018 Martin Benndorf
 * Copyright (c) 2018 Mark Vainomaa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.minidigger.minecraftlauncher.renderer.animation.animations;

import javafx.util.Duration;
import me.minidigger.minecraftlauncher.renderer.SkinTransition;
import me.minidigger.minecraftlauncher.renderer.animation.SkinAnimation;
import me.minidigger.minecraftlauncher.renderer.SkinCanvas;
import me.minidigger.minecraftlauncher.renderer.util.FunctionHelper;

import static me.minidigger.minecraftlauncher.renderer.SkinData.lArm;
import static me.minidigger.minecraftlauncher.renderer.SkinData.lLeg;
import static me.minidigger.minecraftlauncher.renderer.SkinData.rArm;
import static me.minidigger.minecraftlauncher.renderer.SkinData.rLeg;

public class RunningAnimation extends SkinAnimation {

    private SkinTransition rArmTransition;
    private SkinTransition lArmTransition;

    public RunningAnimation(int weight, int time, double angle, SkinCanvas canvas) {
        super(weight);

        lArmTransition = new SkinTransition(Duration.millis(time),
                v -> v * (lArmTransition.getCount() % 4 < 2 ? 1 : -1) * angle,
                lArm.getXRotate().angleProperty(), rLeg.getXRotate().angleProperty());

        rArmTransition = new SkinTransition(Duration.millis(time),
                v -> v * (rArmTransition.getCount() % 4 < 2 ? 1 : -1) * -angle,
                rArm.getXRotate().angleProperty(), lLeg.getXRotate().angleProperty());

        FunctionHelper.alwaysB(SkinTransition::setAutoReverse, true, lArmTransition, rArmTransition);
        FunctionHelper.alwaysB(SkinTransition::setCycleCount, 16, lArmTransition, rArmTransition);
        FunctionHelper.always(transitions::add, lArmTransition, rArmTransition);

        init();
    }
}
