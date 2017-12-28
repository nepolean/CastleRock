//import { _ } from 'lodash';

export class Category {
	public categories = {
		"ASSET": { "SEWAGE": ["SEWAGE"], "PLUMBING": ["PLUMBING"] }, "LEGAL": { "default": ["LEGAL"] }, "BILL_PAYMENT": { "default": ["MANUAL"] }
	}
	constructor() {
	}

	public getCategories(): string[] {
		console.log(Object.keys(this.categories));
		return Object.keys(this.categories);
	}

	public getAmenities(categoryName: string): string[] {
		return Object.keys(this.categories[categoryName]);
	}

	/* public getSkills(categoryName: string, amenityName: string): string[] {
		if (amenityName == undefined || amenityName == null)
			amenityName = "default";
		return this.categories[categoryName][amenityName];
	} */

	public getSkills(categoryName: string, amenityNames: string[]): string[] {
		var skills: string[] = [];
		if (amenityNames == null) {
			amenityNames = []
			amenityNames.push('default');
		}
		for (let amenityName of amenityNames) {
			let skillArray: string[] = this.categories[categoryName][amenityName];			
			for(let skill of skillArray){
				skills.push(skill);
			}
		}
		console.log("skills array",skills);
		return skills;
	}
}