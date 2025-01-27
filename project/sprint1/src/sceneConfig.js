const config = {
  floor: {
    size: { x: 31, y: 24 },
  },
  player: {
    //position: { x: 0.5, y: 0.5 },		//CENTER
    position: { x: 0.1, y: 0.18 }, //INIT
    //position: { x: 0.8, y: 0.85 },		//END
    speed: 0.2,
  },
  sonars: [
    {
      name: "sonar1",
      position: { x: 0.25, y: 0.0 }, //x: 0.55, y: 0.00
      senseAxis: { x: false, y: true },
    },
  ],
  movingObstacles: [],
  staticObstacles: [
    {
      name: "plasticBox",
      centerPosition: { x: 0.34, y: 0.35 },
      size: { x: 0.03, y: 0.07 },
    },

    {
      name: "table",
      centerPosition: { x: 0.7, y: 0.55 },
      size: { x: 0.16, y: 0.2 },
    },
    {
      name: "wallUp",
      centerPosition: { x: 0.44, y: 0.97 },
      size: { x: 0.88, y: 0.02 },
    },
    {
      name: "wallDown",
      centerPosition: { x: 0.44, y: 0.01 },
      size: { x: 0.85, y: 0.01 },
    },
    {
      name: "wallLeft",
      centerPosition: { x: 0.02, y: 0.48 },
      size: { x: 0.01, y: 0.94 },
    },
    {
      name: "wallRight",
      centerPosition: { x: 1, y: 0.5 },
      size: { x: 0.001, y: 0.99 },
    },
  ],
};

export default config;
