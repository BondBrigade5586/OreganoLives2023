# OreganoLives2023
2023 CHARGED UP CODE FOR TEAM 5586 BOND BRIGADE


---- Changelog ----
4/7/23 General Advancements and LED Updates
  - Added startup LED animation (police lights)
  - Idle/disabled mode changed to two singular, alternate lights moving across strip
  - Based autonomous modes off of voltage instead of simple setpoints
    - Allows for autonomous modes to work, even on dead batteries or more charged batteries
    - Removed general uncertainty regarding battery power and autonomous

4/1/23 Post-Seven Rivers Regional
  - Removed 2 cube wired side autonomous modes after failed attempts during practice matches
  - Adjusted setpoints for center autonomous to account for changes in weight distribution, etc
  - Changed LEDControl command slightly
    - Disabled: white breathing
    - Autonomous: white flashing
    - Teleop: alliance color, except when a piece is in the intake (green)

3/25/23 Final Robot Touch-ups
  - Finished center auto (two cubes)
    - Places one, picks up second, climbs out charge station and spits second cube
  - Finished side auto (two cubes)
    - Place two, exits community and aligns with a third if present (regardless of location)
  - Added functioning LED lights on robot
    - Change based on mode: Breathe white when disabled, flash white during autonomous, police lights for teleop (will be changed), endgame lights need to be added
    - Also needs lights when engaged in endgame
    - Includes LEDControl command, which runs when the robot is on, regardless of status (disabled, teleop, etc)

  
3/24/23 Autonomous Advancement
  - Split intake and intake movement into seperate subsystems
  - Two Cube Center Auto Created
  - Two Cube Side Auto Created
    - Places cube low, exits community, then turns and picks up second cube

3/23/23 Intake Movement
  - Able to Move Intake
  - Removed All Commands to Move Limelight (removed functionality on robot)
  - More autonomous testing
  
3/14/23 Limelight & Vision Updates
  - Added functionality to aim Limelight
  - Pick up cube in autonomous
  - Added proximity sensor to hang edge of robot without falling
  - Removed unused drive commands (DriveJoystick, DriveDrift)
  - Temporarily commented LED subsystem (causing issues with moving limelight due to unused PWM port)

3/11/23 Week 2 Competitions: Saturday Meeting
  - Created beta two-cube autonomous mode
  - Added vision pipelines for tracking game pieces & AprilTags
  - Generalized vision commands to allow for all targets instead of only retroreflective tape
  - Changed DriveDistance to ExitCommunity
  - Turned the time the intake runs in autonomous from 5.0 to 0.5 (much more time to engage)
  - Vision command
    1. Removed method "switchLimelightMode"
    2. Added methods to select each vision pipeline (tape, cube, AprilTag, driver camera)

3/9/23 Post-Week 1 Northern Lights Regional
  - Tuned center autonomous to account for added robot weight
  - Button to switch between driver camera and vision processor
  - Driver station tab has been removed (switched to default SmartDashboard tab)
  - Added debug tab

2/26/23 3 Days Until Week 1 Northern Lights Regional
  - Created center autonomous mode
    1. Push preloaded cube out of intake into grid (3 points)
    2. Reverse outside of community (3 points)
    3. Drive forward & engage with charge station (12 points)
  - Removed issues where robot disables/disconnects
  - Automatically select Shuffleboard tab
  - Removed drive control selection dropdown
  - Removed ShootCube command

2/19/23 Sussex Mini-Regional
  - New Shuffleboard tabs 'Setup' and 'Driver Station'
  - Added dropdowns for autonomous mode and drive controls on the setup tab
  - Beta intake subsystem and commands are coded
  - Created side autonomous mode
    1. Push preloaded cube out of intake into grid (3 points)
    2. Reverse outside of community (3 points)
  - Edited button bindings