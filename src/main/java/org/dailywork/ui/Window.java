/*
 * Copyright (c) 2010-2012 Célio Cidral Junior.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.dailywork.ui;

import static org.dailywork.ui.util.Geometry.offset;
import static org.dailywork.ui.util.Geometry.opposite;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.dailywork.config.Options;
import org.dailywork.resources.Images;
import org.dailywork.ui.location.Closest;
import org.dailywork.ui.location.Location;
import org.dailywork.ui.menu.MenuButtonFactory;
import org.dailywork.ui.swing.laf.SexyLabel;
import org.dailywork.ui.swing.laf.SexyPanelUI;

@SuppressWarnings("serial")
public class Window extends JFrame {

    @Inject
    private Options options;
    @Inject
    private Images images;
    @Inject
    private SexyLabel labelFactory;
    @Inject
    private MenuButtonFactory menuButtonFactory;
    @Inject
    private SexyPanelUI panelUI;

    private JPanel panel;
    private JPanel viewport;
    private JLabel projectLabel;

    private WindowDragger dragger = new WindowDragger();
    private boolean gotRelocatedOnceAtLeast;

    @Inject
    public Window(SexyPanelUI panelUI) {
        super("Daily Work");

        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(100, 100);
        setResizable(false);
        setSize(180, 115);
        setUndecorated(true);
        addWindowFocusListener(new HideWindowWhenLosingFocus());
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
    }

    private JPanel createPanel() {
        JPanel mainPanel = new JPanel(new MigLayout());
        mainPanel.setUI(panelUI);

        viewport = new JPanel(new BorderLayout());
        viewport.setOpaque(false);

        JButton menuButton = menuButtonFactory.create();

        JPanel topBar = new JPanel(new MigLayout());
        topBar.setOpaque(false);
        projectLabel = labelFactory.medium();
        projectLabel.setHorizontalAlignment(JLabel.CENTER);

        topBar.add(projectLabel, "dock center");
        topBar.add(menuButton, "dock east");

        mainPanel.add(topBar, "dock north");

        JSeparator separator = new JSeparator();
        separator.setBackground(new Color(15, 15, 15));
        mainPanel.add(separator, "dock north");

        mainPanel.add(viewport, "dock center");

        return mainPanel;
    }

    @PostConstruct
    public void initialize() {
        panel = createPanel();
        setContentPane(panel);

        setIconImages(images.tomatoes());
    }

    public void setProjectName(String projectName) {
        projectLabel.setText(projectName);
    }

    public void setViewportView(Component component) {
        viewport.removeAll();
        viewport.add(component);
    }

    public void show(Point mouseLocation) {
        if (canRelocateWindow(mouseLocation)) {
            Location location = Closest.location(mouseLocation);
            Point point = location.determine(getSize());
            setLocation(point);
            gotRelocatedOnceAtLeast = true;
        }
        setVisible(true);
    }

    private boolean canRelocateWindow(Point mouseLocation) {
        if (options.ui().draggableWindow() && gotRelocatedOnceAtLeast) {
            return false;
        }
        return mouseLocation != null;
    }

    private class HideWindowWhenLosingFocus implements WindowFocusListener {
        @Override
        public void windowGainedFocus(WindowEvent e) {
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
            if (options.ui().autoHideWindow()) {
                setVisible(false);
            }
        }
    }

    private class WindowDragger extends MouseAdapter {

        private Point clickLocation;

        @Override
        public void mousePressed(MouseEvent e) {
            if (leftClicked(e)) {
                clickLocation = e.getPoint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (leftClicked(e)) {
                clickLocation = null;
            }
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            if (clickLocation == null || !options.ui().draggableWindow()) {
                return;
            }
            Point mouseLocation = event.getLocationOnScreen();
            Point windowLocation = offset(opposite(clickLocation), mouseLocation);
            setLocation(windowLocation);
        }

        private boolean leftClicked(MouseEvent e) {
            return e.getButton() == MouseEvent.BUTTON1;
        }

    }

}
