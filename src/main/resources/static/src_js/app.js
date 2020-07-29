const $ = require("jquery");
const axios = require("axios");
const bootstrap = require("bootstrap");

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

// functions
addAlert = (alertTextContent, alertTypeClass) => {
    const alertDismissDelay = 3000;         // in ms
    let alertObj = $('<div class="alert ' + alertTypeClass + ' alert-dismissible fade show"></div>')
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
            if(response.status === 204) {
                // No files

                htmlElements.nextPageButton.attr("disabled", true);
                htmlElements.previousPageButton.attr("disabled", true);
                return;
            }

            if(response.data <= currentPageNumber) {
                // hide
                htmlElements.nextPageButton.attr("disabled", true);
            } else {
                // show
                htmlElements.nextPageButton.attr("disabled", false);
            }

            if(currentPageNumber <= 1) {
                // hide
                htmlElements.previousPageButton.attr("disabled", true);
            } else {
                // show
                htmlElements.previousPageButton.attr("disabled", false);
            }

        })
        .catch(() => {
            addAlert("An error occurred while file paging info!", "alert-warning");
        });
};

updateListOfFileNames = () => {
// Download the freshest data about list of files from server
    axios.get("/api/files/names/paged?page=" + currentPageNumber)
        .then(response => {
            if(response.status !== 204) {
                $("#noFilesFoundParagraph").hide();
                htmlElements.fileList.html("");

                response.data.forEach(function (fileName) {
                    let createdListElement = $('<li class="list-group-item fileListTextAlign"></li>');

                    createdListElement.append($('<span class="fileNameContainer"></span>').text(fileName));
                    createdListElement.append($('<button type="button" class="btnDownload btn-success"></button>').attr("data-filename", fileName).html('<i class="fa fa-upload"></i>'));
                    createdListElement.append($('<button type="button" class="btnDelete btn-danger"></button>').attr("data-filename", fileName).text("X"));

                    htmlElements.fileList.append(createdListElement);
                });
            }
        })
        .catch(() => {
            addAlert("An error occurred while fetching list of files!", "alert-warning");
        });

    axios.get("/api/files/names")
        .then(response => {
            if(response.status === 204) {
                // There aren't any files
                $("#noFilesFoundParagraph").show();
                $("#fileList li").remove();
                $("#filesCountDiv").html("<b>0 files in total</b>");
            } else {
                $("#noFilesFoundParagraph").hide();

                $("#filesCountDiv").html(response.data.length === 1 ? "<b>1 file in total</b>" : ("<b>" + response.data.length + " files in total</b>"));
            }
        })
        .catch(() => {
            addAlert("An error occurred while fetching list of files!", "alert-warning");
        });

    updatePagingButtons();
};
// functions


// executable code
    initPaging();
    updateListOfFileNames();
// executable code

// listeners
htmlElements.getAllFilesInZipButton.on("click", function() {
    if($("#fileList li").length === 0) {
        addAlert("We don't have stored any files!", "alert-warning");
        return;
    }

    axios.get("/api/files/zip")
        .then(response => {
            let zipBlob = new Blob([base64ToArrayBuffer(response.data)], {type: "application/zip"});

            let virtualLink = document.createElement("a");
            virtualLink.href = window.URL.createObjectURL(zipBlob);
            virtualLink.download = "files.zip";
            virtualLink.click();
            virtualLink.remove();
        })
        .catch(() => {
            addAlert("Error occurred! Probably there are duplicates of some file!", "alert-warning");
        });
});

htmlElements.fileList.on("click", "li button.btnDownload", function() {
    let fileName = $(this).attr("data-filename");

    axios.get("/api/files", {
        params: {
            name: fileName
        }
    })
        .then(response => {
            if(response.status !== 200) {
                addAlert(fileName + " not found!", "alert-warning");
                return;
            }

            let fileObject = response.data;
            let zipBlob = new Blob([base64ToArrayBuffer(fileObject.bytes)], {type: fileObject.type});

            let virtualLink = document.createElement("a");
            virtualLink.href = window.URL.createObjectURL(zipBlob);
            virtualLink.download = fileName;
            virtualLink.click();
            virtualLink.remove();
        })
        .catch(() => {
            addAlert(fileName + " not found!", "alert-warning");
        });
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
                addAlert(fileName + " not found!", "alert-warning");
            } else {
                addAlert(fileName + " deleted!", "alert-success");
                updateListOfFileNames();
            }
        })
        .catch(() => {
            addAlert(fileName + " not found!", "alert-warning");
        });
});

htmlElements.addFileForm.on("submit", function(event) {
    event.preventDefault();

    let formData = new FormData(htmlElements.addFileForm[0]);
    htmlElements.addFileForm[0].reset();

    axios.post("/api/files", formData)
        .then(() => {
            addAlert("File uploaded!", "alert-success");
            updateListOfFileNames();
        })
        .catch(error => {
            if(error.response.status === 406) {
                addAlert("File with this name already exists!", "alert-warning");
            } else if(error.response.status === 510) {
                addAlert(error.response.data, "alert-warning");
            } else {
                addAlert("There was a failure when uploading file, try again.", "alert-warning");
            }
        });
});
