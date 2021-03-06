const puppeteer = require('puppeteer');
const assert = require('assert');
const cas = require('../../cas.js');

(async () => {
    const browser = await puppeteer.launch(cas.browserOptions());
    const page = await browser.newPage();
    const url = "https://localhost:8443/cas/oidc/authorize?" +
        "client_id=client&" +
        "redirect_uri=https%3A%2F%2Foidcdebugger.com%2Fdebug&" +
        "scope=openid%20email%20profile%20address%20phone&" +
        "response_type=id_token%20token&" +
        "response_mode=form_post&" +
        "nonce=vn4qulthnx";
    await page.goto(url);

    await cas.loginWith(page, "casuser", "Mellon");
    await page.waitForTimeout(5000)

    await cas.click(page, "#allow");
    await page.waitForNavigation();
    await page.waitForTimeout(5000)

    let header = await cas.textContent(page, "h1.green-text");
    assert(header === "Success!")

    await browser.close();
})();
