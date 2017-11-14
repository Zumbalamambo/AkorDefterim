tinymce.PluginManager.add('akor', function (editor, url) {
    editor.addButton('akor', {
        text: false,
		title : 'Akor',
        image: './img_nota.png',
        onclick: function () {
			editor.selection.setContent('[' + editor.selection.getContent() + ']')
        }
    });
});