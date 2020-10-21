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

package me.minidigger.minecraftlauncher.renderer;

import java.io.IOException;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import me.minidigger.minecraftlauncher.renderer.animation.SkinAnimationPlayer;
import me.minidigger.minecraftlauncher.renderer.model.SkinCube;
import me.minidigger.minecraftlauncher.renderer.model.SkinMultipleCubes;
import me.minidigger.minecraftlauncher.renderer.util.FunctionHelper;
import me.minidigger.minecraftlauncher.renderer.util.SkinHelper;

import static me.minidigger.minecraftlauncher.renderer.SkinData.ALEX_LEFT_ARM;
import static me.minidigger.minecraftlauncher.renderer.SkinData.ALEX_RIGHT_ARM;
import static me.minidigger.minecraftlauncher.renderer.SkinData.STEVEN_LEFT_ARM;
import static me.minidigger.minecraftlauncher.renderer.SkinData.STEVEN_RIGHT_ARM;
import static me.minidigger.minecraftlauncher.renderer.SkinData.body;
import static me.minidigger.minecraftlauncher.renderer.SkinData.bodyInside;
import static me.minidigger.minecraftlauncher.renderer.SkinData.head;
import static me.minidigger.minecraftlauncher.renderer.SkinData.headInside;
import static me.minidigger.minecraftlauncher.renderer.SkinData.lArm;
import static me.minidigger.minecraftlauncher.renderer.SkinData.lArmInside;
import static me.minidigger.minecraftlauncher.renderer.SkinData.lArmOuter;
import static me.minidigger.minecraftlauncher.renderer.SkinData.lLeg;
import static me.minidigger.minecraftlauncher.renderer.SkinData.lLegInside;
import static me.minidigger.minecraftlauncher.renderer.SkinData.rArm;
import static me.minidigger.minecraftlauncher.renderer.SkinData.rArmInside;
import static me.minidigger.minecraftlauncher.renderer.SkinData.rArmOuter;
import static me.minidigger.minecraftlauncher.renderer.SkinData.rLeg;
import static me.minidigger.minecraftlauncher.renderer.SkinData.rLegInside;
import static me.minidigger.minecraftlauncher.renderer.SkinData.scale;
import static me.minidigger.minecraftlauncher.renderer.SkinData.translation;
import static me.minidigger.minecraftlauncher.renderer.SkinData.xRotation;
import static me.minidigger.minecraftlauncher.renderer.SkinData.yRotation;
import static me.minidigger.minecraftlauncher.renderer.SkinData.zRotation;

public class SkinCanvas extends Group {

    private Image skin;
    private boolean isSlim;

    private double preW;
    private double preH;

    private boolean msaa;

    private Group root = new Group();

    private PerspectiveCamera camera = new PerspectiveCamera(true);

    private SkinAnimationPlayer animationPlayer = new SkinAnimationPlayer();

    public SkinCanvas(Image skin, double preW, double preH, boolean msaa) {
        this.skin = skin;
        this.preW = preW;
        this.preH = preH;
        this.msaa = msaa;

        getChildren().add(createSubScene());
    }

    public SkinCanvas(String username, double preW, double preH, boolean msaa) throws IOException {
        this(new Image("https://minotar.net/skin/" + username), preW, preH, msaa);
    }

    public void updateSkin(Image skin, boolean isSlim) {
        if (!SkinHelper.isNoRequest(skin) || !SkinHelper.isSkin(skin))
            return;

        this.skin = SkinHelper.x32Tox64(skin);
        int multiple = Math.max((int) (1024 / skin.getWidth()), 1);

        if (multiple > 1) {
            this.skin = SkinHelper.enlarge(this.skin, multiple);
        }

        if (this.isSlim != isSlim) {
            updateSkinModel(isSlim);
        }

        bindMaterial(root);
    }

    private void updateSkinModel(boolean isSlim) {
        this.isSlim = isSlim;
        FunctionHelper.alwaysB(SkinMultipleCubes::setWidth, isSlim ? 3 : 4, lArmOuter, rArmOuter);
        FunctionHelper.alwaysB(SkinCube::setWidth, isSlim ? 3D : 4D, lArmInside, rArmInside);

        FunctionHelper.alwaysB(Node::setTranslateX, -(bodyInside.getWidth() + lArmInside.getWidth()) / 2, lArm);
        FunctionHelper.alwaysB(Node::setTranslateX, +(bodyInside.getWidth() + rArmInside.getWidth()) / 2, rArm);

        if (isSlim) {
            lArmInside.setModel(ALEX_LEFT_ARM.getModel());
            rArmInside.setModel(ALEX_RIGHT_ARM.getModel());
        } else {
            lArmInside.setModel(STEVEN_LEFT_ARM.getModel());
            rArmInside.setModel(STEVEN_RIGHT_ARM.getModel());
        }

        lArm.getZRotate().setPivotX(-lArmInside.getWidth() / 2);
        rArm.getZRotate().setPivotX(+rArmInside.getWidth() / 2);
    }

    private Material createMaterial() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(skin);
        return material;
    }

    private void bindMaterial(Group group) {
        Material material = createMaterial();
        for (Node node : group.getChildren()) {
            if (node instanceof Shape3D) {
                ((Shape3D) node).setMaterial(material);
            } else if (node instanceof SkinMultipleCubes) {
                ((SkinMultipleCubes) node).updateSkin(skin);
            } else if (node instanceof Group) {
                bindMaterial((Group) node);
            }
        }
    }

    private Group createPlayerModel() {
        head.setTranslateY(-(bodyInside.getHeight() + headInside.getHeight()) / 2);

        lArm.setTranslateX(-(bodyInside.getWidth() + lArmInside.getWidth()) / 2);
        rArm.setTranslateX(+(bodyInside.getWidth() + rArmInside.getWidth()) / 2);

        lLeg.setTranslateX(-(bodyInside.getWidth() - lLegInside.getWidth()) / 2);
        rLeg.setTranslateX(+(bodyInside.getWidth() - rLegInside.getWidth()) / 2);

        lLeg.setTranslateY(+(bodyInside.getHeight() + lLegInside.getHeight()) / 2);
        rLeg.setTranslateY(+(bodyInside.getHeight() + rLegInside.getHeight()) / 2);

        root.getTransforms().addAll(xRotation);

        root.getChildren().addAll(
                head,
                body,
                lArm,
                rArm,
                lLeg,
                rLeg
        );

        updateSkin(skin, false);

        return root;
    }

    private SubScene createSubScene() {
        Group group = new Group();
        group.getChildren().add(createPlayerModel());
        group.getTransforms().add(zRotation);

        camera.getTransforms().addAll(yRotation, translation, scale);

        SubScene subScene = new SubScene(group, preW, preH, true,
                msaa ? SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED);
//        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);

        return subScene;
    }

    public SkinAnimationPlayer getAnimationPlayer() {
        return animationPlayer;
    }

    public Image getSkin() {
        return skin;
    }
}
