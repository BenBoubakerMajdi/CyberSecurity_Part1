import { X2jOptions, XMLParser } from "fast-xml-parser";
import * as fs from "fs";

export type UsesPermission = { "@android:name": string };
export type Action = {
	"@android:name": string;
};
export type Category = {
	"@android:name": string;
};
export type IntentFilter = {
	action: Action;
	category?: Category;
};
export type Activity = {
	"@android:name": string;
	"@android:exported"?: boolean;
	"intent-filter"?: Array<IntentFilter>;
};
export type Application = {
	"@android:name"?: string;
	"@android:label"?: string;
	"@android:icon"?: string;
	"@android:allowBackup"?: boolean;
	activity?: Array<Activity>;
};
export type AndroidManifest = {
	manifest: {
		application: Application;
		"uses-permission"?: Array<UsesPermission>;
	};
};

export class AndroidManifestParser {
	private parser: XMLParser;

	private static readonly TAGS_THAT_CAN_BE_MANY: Array<String> = [
		"uses-permission",
		"activity",
		"intent-filter",
	];

	constructor(options?: X2jOptions) {
		this.parser = new XMLParser({
			...options,
			attributeNamePrefix: "@",
			ignoreAttributes: false,
			parseAttributeValue: true,
			isArray(tagName, _jPath, _isLeafNode, _isAttribute) {
				return AndroidManifestParser.TAGS_THAT_CAN_BE_MANY.includes(
					tagName
				);
			},
		});
	}

	public parse(path: string): AndroidManifest {
		const xml = fs.readFileSync(path, "utf8");
		return this.parser.parse(xml);
	}
}
