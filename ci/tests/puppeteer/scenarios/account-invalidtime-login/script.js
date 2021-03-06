const puppeteer = require('puppeteer');
const assert = require('assert');
const cas = require('../../cas.js');

(async () => {
    const browser = await puppeteer.launch(cas.browserOptions());
    const page = await browser.newPage();
    await page.goto("https://localhost:8443/cas/login");
    await cas.loginWith(page, "invalidtime", "invalidtime");

    // await page.waitForTimeout(5000)

    const header = await cas.innerText(page, '#content h2');

    assert(header === "Your account is forbidden to login at this time.")
    
    await browser.close();
})();
