export class StoryChapter {
  constructor(public id: number,
              public name: string,
              public description: string,
              public strengthRequirement: number,
              public prevChapter: number,
              public nextChapter: number) {}
}
