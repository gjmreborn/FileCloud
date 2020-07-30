const $ = require("jquery");
const axios = require("axios");
require("bootstrap");

const htmlElements = {
    getAllFilesInZipButton: $(".getAllFilesInZip"),
    fileList: $("#fileList"),
    addFileForm: $("#addFileForm"),
    addFileInput: $("#addFileInput"),
    alertDivContainer: $("#alertContainer"),
    pagingDivContainer: $("#pagingContainer"),
    previousPageButton: $("#previousPageButton"),
    nextPageButton: $("#nextPageButton")
};

let currentPageNumber = 1;

addAlert = (alertTextContent, alertTypeClass) => {
    const alertDismissDelay = 3000;
    let alertObj = $(`<div class="alert ${alertTypeClass} alert-dismissible fade show"></div>`)
        .append($('<button class="close" data-dismiss="alert" type="button"><span>&times;</span></span></button>'))
        .append($("<span></span>").text(alertTextContent));


    htmlElements.alertDivContainer.append(alertObj);
    setTimeout(() => alertObj.alert("close"), alertDismissDelay);
};

base64ToArrayBuffer = base64 => {
    const binaryString = window.atob(base64);
    const binaryLen = binaryString.length;

    let bytes = new Uint8Array(binaryLen);
    for (let i = 0; i < binaryLen; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes;
};

initPaging = () => {
    htmlElements.previousPageButton.on("click", () => {
        currentPageNumber--;
        updateListOfFileNames();
    });

    htmlElements.nextPageButton.on("click", () => {
        currentPageNumber++;
        updateListOfFileNames();
    });
};

updatePagingButtons = () => {
    axios.get("/api/files/paging")
        .then(response => {
            const pagesCount = response.data;

            if(pagesCount === 0) {
                htmlElements.nextPageButton.attr("disabled", true);
                htmlElements.previousPageButton.attr("disabled", true);
                return;
            }

            if(pagesCount <= currentPageNumber) {
                htmlElements.nextPageButton.attr("disabled", true);
            } else {
                htmlElements.nextPageButton.attr("disabled", false);
            }

            if(currentPageNumber <= 1) {
                htmlElements.previousPageButton.attr("disabled", true);
            } else {
                htmlElements.previousPageButton.attr("disabled", false);
            }

        })
        .catch(() => addAlert("An error occurred while file paging info!", "alert-warning"));
};

updateListOfFileNames = () => {
    axios.get(`/api/files/names/paged?page=${currentPageNumber}`)
        .then(response => {
            $("#noFilesFoundParagraph").hide();
            htmlElements.fileList.html("");

            const fileNamesPage = response.data;
            fileNamesPage.forEach(fileName => {
                const createdListElement = $('<li class="list-group-item fileListTextAlign"></li>');

                createdListElement.append($('<span class="fileNameContainer"></span>').text(fileName));
                createdListElement.append($('<button type="button" class="btnDownload btn-success"></button>').attr("data-filename", fileName).html('<i class="fa fa-upload"></i>'));
                createdListElement.append($('<button type="button" class="btnDelete btn-danger"></button>').attr("data-filename", fileName).text("X"));

                htmlElements.fileList.append(createdListElement);
            });
        })
        .catch(() => addAlert("An error occurred while fetching list of files!", "alert-warning"));

    axios.get("/api/files/count")
        .then(response => {
            if(response.status === 204) {
                $("#noFilesFoundParagraph").show();
                $("#fileList li").remove();
                $("#filesCountDiv").html("<b>0 files in total</b>");
            } else {
                $("#noFilesFoundParagraph").hide();
                $("#filesCountDiv")
                    .html(response.data === 1 ? "<b>1 file in total</b>" : `<b>${response.data} files in total</b>`);
            }
        })
        .catch(() => addAlert("An error occurred while fetching list of files!", "alert-warning"));

    updatePagingButtons();
};

triggerDownload = (name, type, bytes) => {
    const blob = new Blob([base64ToArrayBuffer(bytes)], {type});

    const virtualLink = document.createElement("a");
    virtualLink.href = window.URL.createObjectURL(blob);
    virtualLink.download = name;
    virtualLink.click();
    virtualLink.remove();
}

initPaging();
updateListOfFileNames();

htmlElements.getAllFilesInZipButton.on("click", () => {
    if($("#fileList li").length === 0) {
        addAlert("We don't have stored any files!", "alert-warning");
        return;
    }

    axios.get("/api/files/zip")
        .then(response => {
            triggerDownload("files.zip", "application/zip", response.data);
        })
        .catch(() => addAlert("Error occurred! Probably there are duplicates of some file!", "alert-warning"));
});

htmlElements.fileList.on("click", "li button.btnDownload", function() {
    const fileName = $(this).attr("data-filename");

    axios.get(`/api/files?name=${fileName}`)
        .then(response => {
            if(response.status !== 200) {
                addAlert(`${fileName} not found!`, "alert-warning");
                return;
            }

            let file = response.data;
            triggerDownload(fileName, file.type, file.bytes);
        })
        .catch(() => addAlert(`${fileName} not found!`, "alert-warning"));
});

htmlElements.fileList.on("click", "li button.btnDelete", function() {
    let fileName = $(this).attr("data-filename");

    axios.delete("/api/files", {
        params: {
            name: fileName
        }
    })
        .then(response => {
            if(response.status !== 200) {
                addAlert(`${fileName} not found!`, "alert-warning");
            } else {
                addAlert(`${fileName} deleted!`, "alert-success");
                updateListOfFileNames();
            }
        })
        .catch(() => addAlert(`${fileName} not found!`, "alert-warning"));
});

htmlElements.addFileForm.on("submit", e => {
    e.preventDefault();

    const formData = new FormData(htmlElements.addFileForm[0]);
    htmlElements.addFileForm[0].reset();

    axios.post("/api/files", formData)
        .then(() => {
            addAlert("File uploaded!", "alert-success");
            updateListOfFileNames();
        })
        .catch(error => {
            if(error.response.status === 406) {
                addAlert("File with this name already exists!", "alert-warning");
            } else if(error.response.status === 400) {
                addAlert(error.response.data, "alert-warning");
            } else {
                addAlert("There was a failure when uploading file, try again.", "alert-warning");
            }
        });
});
