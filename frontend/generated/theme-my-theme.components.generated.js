import { unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin/register-styles';

import vaadinTextFieldCss from 'themes/my-theme/components/vaadin-text-field.css?inline';
import vaadinTextAreaCss from 'themes/my-theme/components/vaadin-text-area.css?inline';
import vaadinPasswordFieldCss from 'themes/my-theme/components/vaadin-password-field.css?inline';
import vaadinButtonCss from 'themes/my-theme/components/vaadin-button.css?inline';


if (!document['_vaadintheme_my-theme_componentCss']) {
  registerStyles(
        'vaadin-text-field',
        unsafeCSS(vaadinTextFieldCss.toString())
      );
      registerStyles(
        'vaadin-text-area',
        unsafeCSS(vaadinTextAreaCss.toString())
      );
      registerStyles(
        'vaadin-password-field',
        unsafeCSS(vaadinPasswordFieldCss.toString())
      );
      registerStyles(
        'vaadin-button',
        unsafeCSS(vaadinButtonCss.toString())
      );
      
  document['_vaadintheme_my-theme_componentCss'] = true;
}

if (import.meta.hot) {
  import.meta.hot.accept((module) => {
    window.location.reload();
  });
}

